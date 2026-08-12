package com.project.jobmatch.domain.support.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.entity.JobSupportProgram;
import com.project.jobmatch.domain.job.repository.JobSupportProgramRepository;
import com.project.jobmatch.domain.support.dto.SupportProgramResponse;
import com.project.jobmatch.domain.support.entity.SupportProgram;
import com.project.jobmatch.domain.support.repository.SupportProgramRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SupportProgramService {
    private final SupportProgramRepository programRepository;
    private final JobSupportProgramRepository jobSupportRepository;

    public SupportProgramService(SupportProgramRepository programRepository,
                                 JobSupportProgramRepository jobSupportRepository) {
        this.programRepository = programRepository;
        this.jobSupportRepository = jobSupportRepository;
    }

    public List<SupportProgramResponse.Summary> getPrograms(Long regionId, Long jobId, String keyword) {
        Set<Long> jobProgramIds = jobId == null ? Set.of() : jobSupportRepository.findAllByJobJobId(jobId).stream()
                .map(JobSupportProgram::getSupportProgram).map(SupportProgram::getProgramId)
                .collect(Collectors.toSet());
        String normalized = normalize(keyword);
        return programRepository.findAll().stream()
                .filter(program -> regionId == null || program.getRegion() == null
                        || Objects.equals(program.getRegion().getRegionId(), regionId))
                .filter(program -> jobId == null || jobProgramIds.contains(program.getProgramId()))
                .filter(program -> normalized == null
                        || program.getProgramName().toLowerCase(Locale.ROOT).contains(normalized)
                        || program.getOrganization() != null
                        && program.getOrganization().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(Comparator.comparing(SupportProgram::getProgramId))
                .map(program -> new SupportProgramResponse.Summary(program.getProgramId(),
                        program.getProgramName(), program.getOrganization(), program.getTargetAudience(),
                        program.getRegion() == null ? "전국" : program.getRegion().getRegionName(),
                        program.getApplyEndDate()))
                .toList();
    }

    public SupportProgramResponse.Detail getProgram(Long programId) {
        SupportProgram program = programRepository.findById(programId)
                .orElseThrow(() -> notFound("SUPPORT_PROGRAM_NOT_FOUND", "해당 지원제도를 찾을 수 없습니다."));
        SupportProgramResponse.Region region = program.getRegion() == null ? null
                : new SupportProgramResponse.Region(program.getRegion().getRegionId(),
                        program.getRegion().getRegionName());
        return new SupportProgramResponse.Detail(program.getProgramId(), program.getProgramName(),
                program.getOrganization(), program.getTargetAudience(), program.getSupportContent(), region,
                program.getApplyStartDate(), program.getApplyEndDate(), program.getExternalUrl(), program.getSource());
    }

    private String normalize(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword.trim().toLowerCase(Locale.ROOT);
    }

    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }
}
