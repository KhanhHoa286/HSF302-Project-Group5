package vn.edu.fpt.hsf302_group5.specification;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.fpt.hsf302_group5.entity.Industry;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.JobPost_;
import vn.edu.fpt.hsf302_group5.entity.Province;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.Recruiter;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JobPostSpecification {

    public static Specification<JobPost> containTitle(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get(JobPost_.title)), "%" + keyword.toLowerCase() + "%"); //chỉ ra thuộc tính title của entity. Là định nghĩa (metadata)
        });
    }

    public static Specification<JobPost> equalTitle(String keyword) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(JobPost_.title), keyword);
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

    public static Specification<JobPost> sortByJobId(String operation) {
        return ((root, query, criteriaBuilder) -> {
            if (operation.equals("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get(JobPost_.jobId)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(JobPost_.jobId)));
            }
            return criteriaBuilder.conjunction();
        });
    }

    public static Specification<JobPost> sortbyTitle(String operation) {
        return ((root, query, criteriaBuilder) -> {
            if (operation.equals("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get(JobPost_.title)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(JobPost_.title)));
            }
            return criteriaBuilder.conjunction();
        });
    }

    public static Specification<JobPost> sortBySalaryMin(String operation) {
        return ((root, query, criteriaBuilder) -> {
            if (operation.equals("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get(JobPost_.salaryMin)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(JobPost_.salaryMin)));
            }
            return criteriaBuilder.conjunction();
        });
    }

    public static Specification<JobPost> sortBySalaryMax(String operation) {
        return ((root, query, criteriaBuilder) -> {
            if (operation.equals("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get(JobPost_.salaryMax)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(JobPost_.salaryMax)));
            }
            return criteriaBuilder.conjunction();
        });
    }

    public static Specification<JobPost> sortByPostedDate(String operation) {
        return ((root, query, criteriaBuilder) -> {
            if (operation.equals("asc")) {
                query.orderBy(criteriaBuilder.asc(root.get(JobPost_.postedDate)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(JobPost_.postedDate)));
            }
            return criteriaBuilder.conjunction();
        });
    }

    public static Specification<JobPost> hasProvice(Integer provinceId) {
        return ((root, query, criteriaBuilder) -> {
            Join<JobPost, Province> province = root.join("province");
            return criteriaBuilder.equal(province.get("provinceId"), provinceId);
        });
    }

    public static Specification<JobPost> hasCompany(Integer companyId) {
        return ((root, query, criteriaBuilder) -> {
            Join<JobPost, Recruiter> recruiter = root.join("recruiter");
            Join<Recruiter, Company> company = recruiter.join("company");
            return criteriaBuilder.equal(company.get("companyId"), companyId);
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

    public static Specification<JobPost> sortBySalaryMin() {
        return ((root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get(JobPost_.salaryMin))); // JPA Static Metamodel để dùng cần bật JPA Static Metamodel Generator. Đây là một annotation processor sẽ tự sinh các lớp Entity_ khi biên dịch.
            return criteriaBuilder.conjunction();
        });
    }

    public static Specification<JobPost> greatThanExpireDate(LocalDate expiredDate) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThan(root.get(JobPost_.expiredDate), expiredDate.atStartOfDay());
        });
    }

    public static Specification<JobPost> equalExpireDate(LocalDate expiredDate) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get(JobPost_.expiredDate), expiredDate.atStartOfDay()), criteriaBuilder.lessThan(root.get(JobPost_.expiredDate), expiredDate.plusDays(1).atStartOfDay()));
        });
    }

    public static Specification<JobPost> greatThanOrEqualExpireDate(LocalDate expiredDate) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(JobPost_.expiredDate), expiredDate.atStartOfDay());
        });
    }

    public static Specification<JobPost> lessThanOrEqualExpireDate(LocalDate expiredDate) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get(JobPost_.expiredDate), expiredDate.atStartOfDay());
        });
    }

    public static Specification<JobPost> lessThanExpireDate(LocalDate expiredDate) {
        return ((root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThan(root.get(JobPost_.expiredDate), expiredDate.atStartOfDay());
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

    public static Specification<JobPost> buildExpireSpec(LocalDate localDate, String s) {
        switch (s.toLowerCase()) {
            case ">":
                return greatThanExpireDate(localDate);
            case ">=":
                return greatThanOrEqualExpireDate(localDate);
            case "<":
                return lessThanExpireDate(localDate);
            case "<=":
                return lessThanOrEqualExpireDate(localDate);
            case "=":
                return equalExpireDate(localDate);
            default:
                return Specification.unrestricted();
        }
    }

    public static Specification<JobPost> bulidSort(String s, String s1) {
        switch (s.toLowerCase()) {
            case "jobId":
                return sortByJobId(s1);
            case "title":
                return sortbyTitle(s1);
            case "salaryMin":
                return sortBySalaryMin(s1);
            case "salaryMax":
                return sortBySalaryMax(s1);
            case "postedDate":
                return sortByPostedDate(s1);
            default:
                return Specification.unrestricted();
        }
    }
}
