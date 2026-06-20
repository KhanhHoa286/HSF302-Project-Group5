package vn.edu.fpt.hsf302_group5.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.StatisticResponse;
import vn.edu.fpt.hsf302_group5.repository.JobPostRepository;
import vn.edu.fpt.hsf302_group5.service.JobPostService;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;


    @Override
    public StatisticResponse getStatistic() {
        return jobPostRepository.getStatistic();
    }
}
