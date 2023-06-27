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
   │  ├──[UACCOUNT]: user is primary
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
      └──[pr_login]: login by (@username or @email) and @password
*/
-- ---------------------------------------------------------------------------------------------------- #TABLES
-- Drop [UACCOUNT] table if already exist then create new [UACCOUNT] table
IF OBJECT_ID('UACCOUNT', 'U') IS NOT NULL DROP TABLE [UACCOUNT]
GO
CREATE TABLE [UACCOUNT] (
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
   [image] varchar(100) primary key,
   [u_id] bigint foreign key references
   [UACCOUNT]([uid]) on delete cascade not null
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
IF OBJECT_ID('UROLES', 'U') IS NOT NULL DROP TABLE [UROLES]
GO
CREATE TABLE [UROLES] (
   [urid] tinyint primary key,
   [role] varchar(20) unique not null
);
GO

-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
-- Drop [US_UA] table if already exist and create new [US_UA] table
IF OBJECT_ID('US_UA', 'U') IS NOT NULL DROP TABLE [US_UA]
GO
CREATE TABLE [US_UA] ( -- USER ACCESS
   [u_id] bigint foreign key references [UACCOUNT]([uid]) on delete cascade not null,
   [ua_id] tinyint foreign key references [UACCESS]([uaid]) not null,
   primary key ([u_id])
);
GO

-- Drop [US_UP] table if already exist and create new [US_UP] table
IF OBJECT_ID('US_UP', 'U') IS NOT NULL DROP TABLE [US_UP]
GO
CREATE TABLE [US_UP] ( -- USER PLATFORMS
   [u_id] bigint foreign key references [UACCOUNT]([uid]) on delete cascade not null,
   [up_id] tinyint foreign key references [UPLATFORM]([upid]) not null,
   primary key ([u_id], [up_id])
);
GO

-- Drop [US_UR] table if already exist and create new [US_UR] table
IF OBJECT_ID('US_UR', 'U') IS NOT NULL DROP TABLE [US_UR]
GO
CREATE TABLE [US_UR] ( -- USER ROLES (authorization)
   [u_id] bigint foreign key references [UACCOUNT]([uid]) on delete cascade not null,
   [ur_id] tinyint foreign key references [UROLES]([urid]) not null,
   primary key ([u_id], [ur_id])
);
GO



-- ---------------------------------------------------------------------------------------------------- #PROCEDURES
-- Create procedure pr_login >>> login by (username or email) and password
IF EXISTS (SELECT [object_id] FROM sys.procedures WHERE name = N'pr_login') DROP PROC pr_login
GO
CREATE PROCEDURE pr_login
   @username varchar(256), @password varchar(256)
AS
BEGIN
   DECLARE @error nvarchar(255);
   IF @username is null OR LEN(@password) = 0 RAISERROR('username is empty',15,1);
   IF @password is null OR LEN(@password) = 0 RAISERROR('password is empty',15,1);

   SELECT u.*, r.ua_id INTO #USER FROM UACCOUNT u LEFT JOIN US_UA r ON u.uid = r.u_id
   WHERE (username = @username OR email = @username) AND PWDCOMPARE(@password, password) = 1;

   IF NOT EXISTS(SELECT [uid] FROM #USER) BEGIN
      SET @error = CONCAT('username:', @username, ' and password:', @password, ' is incorrect');
      RAISERROR(@error, 15,1);
   END ELSE IF ((SELECT ua_id FROM #USER) > 1)
      SELECT * FROM #USER
   ELSE RAISERROR('This account is not activated yet!!!', 15,1);
END
GO

-- Create procedure pr_update_pass >>> update password by username or email
IF EXISTS (SELECT [object_id] FROM sys.procedures WHERE name = N'pr_update_pass') DROP PROC pr_login
GO
CREATE PROCEDURE pr_update_pass
   @unique varchar(256), @password varchar(256)
AS
BEGIN
   DECLARE @err nvarchar(256);
   DECLARE @id bigint = (SELECT [uid] FROM UACCOUNT WHERE @unique IN ([username], [email]));

   IF @id IS NOT NULL BEGIN
      UPDATE UACCOUNT SET [password]=PWDENCRYPT(@password) WHERE [uid]=@id;
      SELECT [password] FROM UACCOUNT WHERE [uid]=@id;
   END ELSE BEGIN
      SET @err = CONCAT(@unique, ' does not exist, cannot update this password:', @password)
      RAISERROR(@err, 15,1);
   END
END
GO

USE master
