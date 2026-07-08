package vn.edu.fpt.hsf302_group5.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.fpt.hsf302_group5.entity.Industry;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.Province;

import java.math.BigDecimal;

public class JobPostSpecification {

    public static Specification<JobPost> containTitle(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase() + "%");
        });
    }

    public static Specification<JobPost> equalTitle(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("title"), keyword);
        });
    }

    public static Specification<JobPost> startWithTitle(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), keyword.toLowerCase() + "%");
        });
    }

    public static Specification<JobPost> endWithTitle(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + keyword.toLowerCase());
        });
    }

    public static Specification<JobPost> hasIndustry(Integer industryid) {
        return (((root, query, criteriaBuilder) -> {
            Join<JobPost, Industry> industry = root.join("industry");
            return criteriaBuilder.equal(industry.get("industryId"), industryid);
        }));
    }

    public static Specification<JobPost> hasProvice(Integer provinceId) {
        return ((root, query, criteriaBuilder) -> {
            Join<JobPost, Province> province = root.join("province");
            return criteriaBuilder.equal(province.get("provinceId"), provinceId);
        });
    }

    public static Specification<JobPost> greatThanOrEqualSalary(BigDecimal salary) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("salaryMin"), salary);
        });
    }

    public static Specification<JobPost> lessThanOrEqualSalary(BigDecimal salary) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("salaryMax"), salary);
        });
    }

    public static Specification<JobPost> lessThanSalary(BigDecimal salary) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get("salaryMax"), salary);
        });
    }

    public static Specification<JobPost> greatThanSalary(BigDecimal salary) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get("salaryMin"), salary);
        });
    }

    public static Specification<JobPost> buildTitleSpec(String operator, String value) {
        switch (operator.toLowerCase()) {
            case "contains":
                return JobPostSpecification.containTitle(value);
            case "equals":
                return JobPostSpecification.equalTitle(value);
            case "starts with":
                return JobPostSpecification.startWithTitle(value);
            case "ends with":
                return JobPostSpecification.endWithTitle(value);
            default:
                return Specification.unrestricted();
        }
    }

    public static Specification<JobPost> buildSalarySpec(String s, BigDecimal bigDecimal) {
        switch (s.toLowerCase()) {
            case ">=":
                return JobPostSpecification.greatThanOrEqualSalary(bigDecimal);
            case ">":
                return JobPostSpecification.greatThanSalary(bigDecimal);
            case "<=":
                return JobPostSpecification.lessThanOrEqualSalary(bigDecimal);
            case "<":
                return JobPostSpecification.lessThanSalary(bigDecimal);
            default:
                return Specification.unrestricted();
        }
    }
}
