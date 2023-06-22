USE master
GO
-- -------------------------------------------------- CREATE DATABASE --------------------------------------------------
-- Drop database if exist
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'DB_SUPER')
   DROP DATABASE [DB_SUPER]
GO
-- Create database
CREATE DATABASE [DB_SUPER]
GO

-- -------------------------------------------------- CREATE TABLES --------------------------------------------------
USE [DB_SUPER]
GO

/*
   ------------------------------ USER AND AUTHORIZATIONS ------------------------------
   - CRM
   ├──[#TABLES]
   │  ├──[USER]: user is primary
   │  ├──[UIMAGE]: user's images | one-many
   │  │
   │  ├──[UACCESS]: access for user
   │  ├──[UPLATFORM]: login platform information
   │  ├──[UROLE]: role for user
   │  │
   │  ├──[US_UA]: unique user access relationship | one-one
   │  ├──[US_UP]: user references platform | one-many
   │  └──[US_UR]: user has multiple roles | one-many
   │
   └──[#PROCEDURES]


*/
-- ---------------------------------------------------------------------------------------------------- #TABLES
-- Drop [USER] table if already exist then create new [USER] table
IF OBJECT_ID('USER', 'U') IS NOT NULL DROP TABLE [USER]
GO
CREATE TABLE [USER] (
   [uid] bigint identity primary key,
   [username] varchar(20) null unique, -- username for login
   [email] varchar(50) unique not null, -- email for contact
   [password] binary(70) not null, -- size of PWDENCRYPT = 70
   [fullname] nvarchar(50) not null
);
GO

-- Drop [UIMAGE] table if already exist then create new [UIMAGE] table
IF OBJECT_ID('UIMAGE', 'U') IS NOT NULL DROP TABLE [UIMAGE]
GO
CREATE TABLE [UIMAGE] (
   [u_id] bigint foreign key references
   [USER]([uid]) on delete cascade not null,
   [image] varchar(100) unique not null,
   primary key([u_id], [image])
);
GO

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Drop [UPLATFORM] table if already exist then create new [UPLATFORM] table
IF OBJECT_ID('UPLATFORM', 'U') IS NOT NULL DROP TABLE [UPLATFORM]
GO
CREATE TABLE [UPLATFORM] (
   [upid] tinyint primary key,
   [up_name] nvarchar(20) unique not null,
   [up_other] varchar(100) -- other info
);
GO

-- Drop [UACCESS] table if already exist and create new [UACCESS] table
IF OBJECT_ID('[UACCESS]', 'U') IS NOT NULL DROP TABLE [UACCESS]
GO
CREATE TABLE [UACCESS] (
   [uaid] tinyint primary key,
   [ua_name] nvarchar(10) unique not null
);
GO

-- Drop [ROLES] table if already exist and create new [ROLES] table
IF OBJECT_ID('ROLES', 'U') IS NOT NULL DROP TABLE [ROLES]
GO
CREATE TABLE [ROLES] (
   [urid] tinyint primary key,
   [role] nvarchar(50) unique not null
);
GO

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Drop [US_UA] table if already exist and create new [US_UA] table
IF OBJECT_ID('US_UA', 'U') IS NOT NULL DROP TABLE [US_UA]
GO
CREATE TABLE [US_UA] ( -- USER ACCESS
   [u_id] bigint foreign key references [USER]([uid]) on delete cascade not null,
   [ua_id] tinyint foreign key references [UACCESS]([uaid]) not null,
   primary key ([u_id])
);
GO

-- Drop [US_UP] table if already exist and create new [US_UP] table
IF OBJECT_ID('US_UP', 'U') IS NOT NULL DROP TABLE [US_UP]
GO
CREATE TABLE [US_UP] ( -- USER PLATFORMS
   [u_id] bigint foreign key references [USER]([uid]) on delete cascade not null,
   [up_id] tinyint foreign key references [UPLATFORM]([upid]) not null,
   primary key ([u_id], [up_id])
);
GO

-- Drop [US_UR] table if already exist and create new [US_UR] table
IF OBJECT_ID('US_UR', 'U') IS NOT NULL DROP TABLE [US_UR]
GO
CREATE TABLE [US_UR] ( -- USER ROLES (authorization)
   [u_id] bigint foreign key references [USER]([uid]) on delete cascade not null,
   [ur_id] tinyint foreign key references [ROLES]([urid]) not null,
   primary key ([u_id], [ur_id])
);
GO





-- ---------------------------------------------------------------------------------------------------- #PROCEDURES
-- Create procedure login >>> login by (username or email) and password
IF EXISTS (SELECT [object_id] FROM sys.procedures WHERE name = N'pr_login') DROP PROC pr_login
GO
CREATE PROCEDURE pr_login
   @username varchar(256), @password varchar(256)
AS
BEGIN
   DECLARE @meserror nvarchar(256);
   DECLARE @ua_id tinyint;

   IF @username is null OR LEN(@password) = 0 RAISERROR('username is empty',15,1);
   IF @password is null OR LEN(@password) = 0 RAISERROR('password is empty',15,1);

   SELECT u.* INTO #USER FROM [USER] u
   WHERE (username = @username OR email = @username) AND PWDCOMPARE(@password, password) = 1;

   -- default active is null or access id = 0
   IF EXISTS(SELECT username FROM #USER) BEGIN
      SET @ua_id = (SELECT a.[ua_id] FROM [US_UA] a JOIN [#USER] b ON a.u_id=b.uid);
      IF (@ua_id = 0 OR @ua_id IS NULL) SELECT * FROM #USER;
      ELSE RAISERROR('This account is not activated yet!!!', 15,1);
   END ELSE BEGIN
      SET @meserror = CONCAT ('username:', @username, ' and password:', @password, ' is incorrect');
      RAISERROR(@meserror, 12,1);
   END
END
GO
