USE [HSF-Group-Project]

-- =========================
-- USERS
-- =========================
INSERT INTO Users
(Email, PasswordHash, FullName, Phone, AvatarUrl, Role, Status)
VALUES
('admin@jobhub.com', 'hashed123', N'Nguyễn Quản Trị', '0901000001', NULL, 'ADMIN', 'ACTIVE'),

('recruiter1@fpt.com', 'hashed123', N'Trần Minh HR', '0901000002', NULL, 'RECRUITER', 'ACTIVE'),
('recruiter2@viettel.com', 'hashed123', N'Lê Thu Hà', '0901000003', NULL, 'RECRUITER', 'ACTIVE'),

('candidate1@gmail.com', 'hashed123', N'Nguyễn Văn An', '0901000004', NULL, 'CANDIDATE', 'ACTIVE'),
('candidate2@gmail.com', 'hashed123', N'Trần Thị Mai', '0901000005', NULL, 'CANDIDATE', 'ACTIVE'),
('candidate3@gmail.com', 'hashed123', N'Phạm Đức Long', '0901000006', NULL, 'CANDIDATE', 'ACTIVE');

-- =========================
-- COMPANY
-- =========================
INSERT INTO Company
(CompanyName, LogoUrl, Website, Description, Address)
VALUES
(N'FPT Software',
'https://logo.clearbit.com/fpt.com.vn',
'https://fptsoftware.com',
N'Công ty phần mềm hàng đầu Việt Nam',
N'Hà Nội'),

(N'Viettel Solutions',
'https://logo.clearbit.com/viettel.com.vn',
'https://viettel.com.vn',
N'Công ty công nghệ thuộc tập đoàn Viettel',
N'Hà Nội');

-- =========================
-- RECRUITER
-- UserID 2,3 là recruiter
-- =========================
INSERT INTO Recruiter
(RecruiterID, CompanyID)
VALUES
(2,1),
(3,2);


-- =========================
-- INDUSTRY
-- =========================
INSERT INTO Industry
(IndustryName)
VALUES
(N'Information Technology'),
(N'Artificial Intelligence'),
(N'Cyber Security'),
(N'Business Analyst');

-- =========================
-- COMPANY INDUSTRY
-- =========================
INSERT INTO CompanyIndustry
VALUES
(1,1),
(1,2),
(2,1),
(2,3);

-- =========================
-- CANDIDATE PROFILE
-- UserID 4,5,6
-- =========================
INSERT INTO CandidateProfile
(CandidateID, DateOfBirth, Gender, Address, Summary)
VALUES
(4,'2002-05-10','MALE',
N'Hà Nội',
N'Sinh viên CNTT yêu thích Java Backend'),

(5,'2001-08-15','FEMALE',
N'Đà Nẵng',
N'Frontend Developer với ReactJS'),

(6,'2000-11-20','MALE',
N'TP Hồ Chí Minh',
N'Data Analyst và SQL Developer');

-- =========================
-- SKILL
-- =========================
INSERT INTO Skill
(SkillName)
VALUES
(N'Java'),
(N'Spring Boot'),
(N'SQL Server'),
(N'ReactJS'),
(N'Python'),
(N'Power BI');

-- =========================
-- CANDIDATE SKILL
-- =========================
INSERT INTO CandidateSkill
VALUES
(4,1),
(4,2),
(4,3),

(5,4),

(6,3),
(6,5),
(6,6);

-- =========================
-- EDUCATION
-- =========================
INSERT INTO Education
(CandidateID, SchoolName, Degree, Major, StartDate, EndDate)
VALUES
(4,N'Đại học FPT',N'Cử nhân',N'Kỹ thuật phần mềm','2020-09-01','2024-06-30'),

(5,N'Đại học Bách Khoa Đà Nẵng',N'Cử nhân',N'Công nghệ thông tin','2019-09-01','2023-06-30'),

(6,N'Đại học Kinh tế TP.HCM',N'Cử nhân',N'Hệ thống thông tin','2018-09-01','2022-06-30');


-- =========================
-- EXPERIENCE
-- =========================
INSERT INTO Experience
(CandidateID, CompanyName, Position, Description, StartDate, EndDate)
VALUES
(4,
N'TMA Solutions',
N'Java Intern',
N'Phát triển REST API bằng Spring Boot',
'2023-01-01',
'2023-08-31'),

(5,
N'NashTech',
N'Frontend Developer',
N'Xây dựng giao diện ReactJS',
'2023-02-01',
NULL),

(6,
N'KPMG',
N'Data Analyst',
N'Xây dựng dashboard Power BI',
'2022-07-01',
NULL);

-- =========================
-- CV
-- =========================
INSERT INTO CV
(CandidateID, CVName, FileName, FileUrl)
VALUES
(4,
N'Java Backend CV',
N'java_backend_cv.pdf',
'https://storage.blob.core.windows.net/cv/java_backend_cv.pdf'),

(5,
N'Frontend CV',
N'frontend_cv.pdf',
'https://storage.blob.core.windows.net/cv/frontend_cv.pdf'),

(6,
N'Data Analyst CV',
N'data_cv.pdf',
'https://storage.blob.core.windows.net/cv/data_cv.pdf');

-- =========================
-- JOB POST
-- =========================
INSERT INTO JobPost
(
    RecruiterID,
    IndustryID,
    JobLevel,
    Vacancies,
    Title,
    Description,
    Requirement,
    Location,
    SalaryMin,
    SalaryMax,
    EmploymentType,
    Status,
    ExpiredDate,
    ApprovedBy,
    ApprovedDate
)
VALUES
(
    2,
    1,
    'JUNIOR',
    3,
    N'Java Backend Developer',
    N'Phát triển hệ thống Spring Boot',
    N'Java, Spring Boot, SQL',
    N'Hà Nội',
    12000000,
    18000000,
    'FULL_TIME',
    'APPROVED',
    DATEADD(DAY, 30, GETDATE()),
    1,
    GETDATE()
),

(
    3,
    3,
    'SENIOR',
    2,
    N'Cyber Security Engineer',
    N'Giám sát an toàn thông tin',
    N'Network, Security',
    N'Hà Nội',
    15000000,
    25000000,
    'FULL_TIME',
    'APPROVED',
    DATEADD(DAY, 30, GETDATE()),
    1,
    GETDATE()
),

(
    2,
    2,
    'MID',
    2,
    N'AI Engineer',
    N'Xây dựng mô hình Machine Learning',
    N'Python, AI, Deep Learning',
    N'TP Hồ Chí Minh',
    18000000,
    30000000,
    'FULL_TIME',
    'PENDING',
    DATEADD(DAY, 30, GETDATE()),
    NULL,
    NULL
);

-- =========================
-- SAVED JOB
-- =========================
INSERT INTO SavedJob
(CandidateID, JobID)
VALUES
(4,1),
(5,1),
(6,2);

-- =========================
-- APPLICATION
-- =========================
INSERT INTO Application
(
CandidateID,
JobID,
CVID,
CoverLetter,
Status
)
VALUES
(
4,
1,
1,
N'Tôi mong muốn phát triển sự nghiệp Java Backend.',
'UNDER_REVIEW'
),

(
6,
2,
3,
N'Tôi có kinh nghiệm về phân tích dữ liệu và bảo mật.',
'SHORTLISTED'
);

-- =========================
-- APPLICATION STATUS HISTORY
-- =========================
INSERT INTO ApplicationStatusHistory
(
ApplicationID,
OldStatus,
NewStatus,
ChangedBy
)
VALUES
(1,'APPLIED','UNDER_REVIEW',2),
(2,'UNDER_REVIEW','SHORTLISTED',3);

-- =========================
-- INTERVIEW
-- =========================
INSERT INTO Interview
(
ApplicationID,
InterviewDate,
Location,
MeetingLink,
Note,
Status
)
VALUES
(
2,
DATEADD(DAY,5,GETDATE()),
N'Viettel Tower',
NULL,
N'Phỏng vấn vòng kỹ thuật',
'SCHEDULED'
);