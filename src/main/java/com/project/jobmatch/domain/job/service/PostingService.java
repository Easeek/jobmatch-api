package com.project.jobmatch.domain.job.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.dto.PostingResponse;
import com.project.jobmatch.domain.job.entity.JobPosting;
import com.project.jobmatch.domain.job.repository.JobPostingRepository;
import com.project.jobmatch.domain.user.entity.WorkType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PostingService {
    private final JobPostingRepository postingRepository;

    public PostingService(JobPostingRepository postingRepository) {
        this.postingRepository = postingRepository;
    }

    public List<PostingResponse.Summary> getPostings(Long regionId, Long jobId, WorkType workType,
                                                     String keyword, String sort) {
        String normalized = normalize(keyword);
        Comparator<JobPosting> comparator = postingComparator(sort);
        return postingRepository.findAll().stream()
                .filter(posting -> regionId == null || posting.getRegion() != null
                        && Objects.equals(posting.getRegion().getRegionId(), regionId))
                .filter(posting -> jobId == null || posting.getJob() != null
                        && Objects.equals(posting.getJob().getJobId(), jobId))
                .filter(posting -> workType == null || posting.getWorkType() == workType)
                .filter(posting -> normalized == null
                        || posting.getTitle().toLowerCase(Locale.ROOT).contains(normalized)
                        || posting.getCompanyName() != null
                        && posting.getCompanyName().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(comparator)
                .map(posting -> new PostingResponse.Summary(posting.getPostingId(), posting.getTitle(),
                        posting.getCompanyName(), posting.getRegion() == null ? null
                        : posting.getRegion().getRegionName(), posting.getWorkType(), posting.getCareerLevel(),
                        posting.getApplyDeadline()))
                .toList();
    }

    public PostingResponse.Detail getPosting(Long postingId) {
        JobPosting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> notFound("POSTING_NOT_FOUND", "해당 채용공고를 찾을 수 없습니다."));
        PostingResponse.Job job = posting.getJob() == null ? null
                : new PostingResponse.Job(posting.getJob().getJobId(), posting.getJob().getJobName());
        PostingResponse.Region region = posting.getRegion() == null ? null
                : new PostingResponse.Region(posting.getRegion().getRegionId(), posting.getRegion().getRegionName());
        return new PostingResponse.Detail(posting.getPostingId(), job, posting.getTitle(),
                posting.getCompanyName(), region, posting.getWorkType(), posting.getRequiredEducation(),
                posting.getCareerLevel(), posting.getSalaryText(), posting.getApplyDeadline(),
                posting.getExternalUrl(), posting.getSource());
    }

    private Comparator<JobPosting> postingComparator(String sort) {
        if (sort == null || sort.isBlank() || sort.equalsIgnoreCase("latest")) {
            return Comparator.comparing(JobPosting::getCreatedAt,
                    Comparator.nullsLast(Comparator.reverseOrder())).thenComparing(JobPosting::getPostingId);
        }
        if (sort.equalsIgnoreCase("deadline")) {
            return Comparator.comparing(JobPosting::getApplyDeadline,
                    Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(JobPosting::getPostingId);
        }
        throw new CustomException(HttpStatus.BAD_REQUEST, "INVALID_SORT", "sort는 deadline 또는 latest여야 합니다.");
    }

    private String normalize(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword.trim().toLowerCase(Locale.ROOT);
    }

    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }
}
