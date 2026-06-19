-- Nếu DB đã tồn tại thì xóa
USE master
IF
EXISTS (
    SELECT *
    FROM sys.databases
    WHERE name = 'HSF-Group-Project'
)
BEGIN
    ALTER
DATABASE [HSF-Group-Project]
    SET SINGLE_USER
    WITH ROLLBACK IMMEDIATE;

    DROP
DATABASE [HSF-Group-Project];
END
GO

-- Tạo mới Database

CREATE
DATABASE [HSF-Group-Project];
GO

-- Sử dụng Database

USE [HSF-Group-Project];
GO
-- =========================
-- USERS
-- =========================
CREATE TABLE Users
(
    UserID       INT IDENTITY(1,1) PRIMARY KEY,

    Email        VARCHAR(255) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,

    FullName     NVARCHAR(100) NOT NULL,
    Phone        VARCHAR(20),

    AvatarUrl    NVARCHAR(500),

    Role         VARCHAR(20)  NOT NULL
        CHECK (Role IN ('ADMIN', 'CANDIDATE', 'RECRUITER')),

    Status       VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE'
        CHECK (Status IN ('ACTIVE', 'INACTIVE')),

    CreatedAt    DATETIME2             DEFAULT GETDATE(),
    UpdatedAt    DATETIME2 NULL
);

-- =========================
-- COMPANY
-- =========================
CREATE TABLE Company
(
    CompanyID   INT IDENTITY(1,1) PRIMARY KEY,
    CompanyName NVARCHAR(200) NOT NULL,
    LogoUrl     NVARCHAR(500), -- Sửa thành NVARCHAR
    Website     VARCHAR(255),
    Description NVARCHAR(MAX),
    Address     NVARCHAR(255),
    Status      VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        CHECK (Status IN ('ACTIVE', 'INACTIVE')),
    CreatedAt   DATETIME2            DEFAULT GETDATE(),
    UpdatedAt   DATETIME2 NULL
);


CREATE TABLE Recruiter
(
    RecruiterID INT PRIMARY KEY,

    CompanyID   INT NOT NULL,

    FOREIGN KEY (RecruiterID)
        REFERENCES Users (UserID),

    FOREIGN KEY (CompanyID)
        REFERENCES Company (CompanyID)
);

-- =========================
-- INDUSTRY
-- =========================
CREATE TABLE Industry
(
    IndustryID   INT IDENTITY(1,1) PRIMARY KEY,
    IndustryName NVARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE CompanyIndustry
(
    CompanyID  INT NOT NULL,
    IndustryID INT NOT NULL,

    PRIMARY KEY (CompanyID, IndustryID),

    FOREIGN KEY (CompanyID)
        REFERENCES Company (CompanyID),

    FOREIGN KEY (IndustryID)
        REFERENCES Industry (IndustryID)
);


-- =========================
-- CANDIDATE PROFILE
-- =========================
CREATE TABLE CandidateProfile
(
    CandidateID INT PRIMARY KEY,

    DateOfBirth DATE,

    Gender      VARCHAR(10)
        CHECK (Gender IN ('MALE', 'FEMALE', 'OTHER')),

    Address     NVARCHAR(255),

    Summary     NVARCHAR(MAX),

    FOREIGN KEY (CandidateID)
        REFERENCES Users (UserID)
);

-- =========================
-- SKILL
-- =========================
CREATE TABLE Skill
(
    SkillID   INT IDENTITY(1,1) PRIMARY KEY,
    SkillName NVARCHAR(100) NOT NULL UNIQUE
);

-- =========================
-- CANDIDATE SKILL
-- =========================
CREATE TABLE CandidateSkill
(
    CandidateID INT NOT NULL,
    SkillID     INT NOT NULL,

    PRIMARY KEY (CandidateID, SkillID),

    FOREIGN KEY (CandidateID)
        REFERENCES CandidateProfile (CandidateID),

    FOREIGN KEY (SkillID)
        REFERENCES Skill (SkillID)
);

-- =========================
-- EDUCATION
-- =========================
CREATE TABLE Education
(
    EducationID INT IDENTITY(1,1) PRIMARY KEY,

    CandidateID INT NOT NULL,

    SchoolName  NVARCHAR(200) NOT NULL,
    Degree      NVARCHAR(100),
    Major       NVARCHAR(100),

    StartDate   DATE,
    EndDate     DATE,

    FOREIGN KEY (CandidateID)
        REFERENCES CandidateProfile (CandidateID)
);

-- =========================
-- EXPERIENCE
-- =========================
CREATE TABLE Experience
(
    ExperienceID INT IDENTITY(1,1) PRIMARY KEY,

    CandidateID  INT NOT NULL,

    CompanyName  NVARCHAR(200),
    Position     NVARCHAR(100),

    Description  NVARCHAR(MAX),

    StartDate    DATE,
    EndDate      DATE,

    FOREIGN KEY (CandidateID)
        REFERENCES CandidateProfile (CandidateID)
);

-- =========================
-- CV
-- =========================
CREATE TABLE CV
(
    CVID        INT IDENTITY(1,1) PRIMARY KEY,

    CandidateID INT          NOT NULL,

    CVName      NVARCHAR(100) NOT NULL,

    FileName    NVARCHAR(255) NOT NULL,
    FileUrl     VARCHAR(500) NOT NULL,

    UploadedAt  DATETIME2 DEFAULT GETDATE(),

    FOREIGN KEY (CandidateID)
        REFERENCES CandidateProfile (CandidateID)
);

-- =========================
-- JOB POST
-- =========================
CREATE TABLE JobPost
(
    JobID          INT IDENTITY(1,1) PRIMARY KEY,
    RecruiterID    INT         NOT NULL,    -- Thay vì CompanyID, chuyển thành RecruiterID để biết ai đăng
    IndustryID     INT         NOT NULL,
    JobLevel       VARCHAR(30),
    Vacancies      INT                  DEFAULT 1,
    Title          NVARCHAR(200) NOT NULL,
    Description    NVARCHAR(MAX) NOT NULL,
    Requirement    NVARCHAR(MAX),
    Location       NVARCHAR(200),
    SalaryMin      DECIMAL(18, 2),
    SalaryMax      DECIMAL(18, 2),
    EmploymentType VARCHAR(30),
    Status         VARCHAR(30) NOT NULL DEFAULT 'PENDING'
        CHECK (Status IN (
                          'PENDING',
                          'APPROVED',
                          'REJECTED',
                          'CLOSED'
            )),

    PostedDate     DATETIME2            DEFAULT GETDATE(),
    ExpiredDate    DATETIME2 NULL,

    ApprovedBy     INT NULL,
    ApprovedDate   DATETIME2 NULL,
    AdminComment   NVARCHAR(500),

    CONSTRAINT CK_Salary
        CHECK (
            SalaryMin IS NULL
                OR SalaryMax IS NULL
                OR SalaryMin <= SalaryMax
            ),

    FOREIGN KEY (RecruiterID)
        REFERENCES Recruiter (RecruiterID), -- Liên kết tới Recruiter

    FOREIGN KEY (IndustryID)
        REFERENCES Industry (IndustryID),

    FOREIGN KEY (ApprovedBy)
        REFERENCES Users (UserID)
);

-- =========================
-- SAVED JOB
-- =========================
CREATE TABLE SavedJob
(
    CandidateID INT NOT NULL,
    JobID       INT NOT NULL,

    SavedAt     DATETIME2 DEFAULT GETDATE(),

    PRIMARY KEY (CandidateID, JobID),

    FOREIGN KEY (CandidateID)
        REFERENCES CandidateProfile (CandidateID),

    FOREIGN KEY (JobID)
        REFERENCES JobPost (JobID)
);

-- =========================
-- APPLICATION
-- =========================
CREATE TABLE Application
(
    ApplicationID INT IDENTITY(1,1) PRIMARY KEY,

    CandidateID   INT         NOT NULL,
    JobID         INT         NOT NULL,
    CVID          INT         NOT NULL,

    AppliedDate   DATETIME2            DEFAULT GETDATE(),
    CoverLetter   NVARCHAR(MAX),

    Status        VARCHAR(30) NOT NULL DEFAULT 'APPLIED'
        CHECK (Status IN (
                          'APPLIED',
                          'UNDER_REVIEW',
                          'SHORTLISTED',
                          'INTERVIEWED',
                          'ACCEPTED',
                          'REJECTED'
            )),

    Note          NVARCHAR(500),

    CONSTRAINT UQ_Application
        UNIQUE (CandidateID, JobID),

    FOREIGN KEY (CandidateID)
        REFERENCES CandidateProfile (CandidateID),

    FOREIGN KEY (JobID)
        REFERENCES JobPost (JobID),

    FOREIGN KEY (CVID)
        REFERENCES CV (CVID)
);

-- =========================
-- APPLICATION STATUS HISTORY
-- =========================
CREATE TABLE ApplicationStatusHistory
(
    HistoryID     INT IDENTITY(1,1) PRIMARY KEY,

    ApplicationID INT NOT NULL,

    OldStatus     VARCHAR(30),
    NewStatus     VARCHAR(30),

    ChangedBy     INT NOT NULL,

    ChangedAt     DATETIME2 DEFAULT GETDATE(),

    FOREIGN KEY (ApplicationID)
        REFERENCES Application (ApplicationID),

    FOREIGN KEY (ChangedBy)
        REFERENCES Users (UserID)
);

CREATE TABLE Interview
(
    InterviewID   INT IDENTITY(1,1) PRIMARY KEY,

    ApplicationID INT       NOT NULL,

    InterviewDate DATETIME2 NOT NULL,

    Location      NVARCHAR(255),
    MeetingLink   VARCHAR(500),

    Note          NVARCHAR(500),

    CreatedAt     DATETIME2 DEFAULT GETDATE(),
    Status        VARCHAR(20)
                            DEFAULT 'SCHEDULED'
        CHECK (
            Status IN (
                       'SCHEDULED',
                       'COMPLETED',
                       'CANCELLED'
                )
            ),

    FOREIGN KEY (ApplicationID)
        REFERENCES Application (ApplicationID)
);