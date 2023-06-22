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
   │  ├──[ACCOUNT]: user is primary
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
-- Drop [ACCOUNT] table if already exist then create new [ACCOUNT] table
IF OBJECT_ID('USER', 'U') IS NOT NULL DROP TABLE [ACCOUNT]
GO
CREATE TABLE [ACCOUNT] (
   [uid] bigint identity primary key,
   [username] varchar(20) null unique, -- username for login
   [email] varchar(50) unique not null, -- email for contact
   [password] varchar(70) not null, -- size of PWDENCRYPT = 70
   [fullname] nvarchar(50) not null
);
GO

-- Drop [UIMAGE] table if already exist then create new [UIMAGE] table
IF OBJECT_ID('UIMAGE', 'U') IS NOT NULL DROP TABLE [UIMAGE]
GO
CREATE TABLE [UIMAGE] (
   [u_id] bigint foreign key references
   [ACCOUNT]([uid]) on delete cascade not null,
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
   [u_id] bigint foreign key references [ACCOUNT]([uid]) on delete cascade not null,
   [ua_id] tinyint foreign key references [UACCESS]([uaid]) not null,
   primary key ([u_id])
);
GO

-- Drop [US_UP] table if already exist and create new [US_UP] table
IF OBJECT_ID('US_UP', 'U') IS NOT NULL DROP TABLE [US_UP]
GO
CREATE TABLE [US_UP] ( -- USER PLATFORMS
   [u_id] bigint foreign key references [ACCOUNT]([uid]) on delete cascade not null,
   [up_id] tinyint foreign key references [UPLATFORM]([upid]) not null,
   primary key ([u_id], [up_id])
);
GO

-- Drop [US_UR] table if already exist and create new [US_UR] table
IF OBJECT_ID('US_UR', 'U') IS NOT NULL DROP TABLE [US_UR]
GO
CREATE TABLE [US_UR] ( -- USER ROLES (authorization)
   [u_id] bigint foreign key references [ACCOUNT]([uid]) on delete cascade not null,
   [ur_id] tinyint foreign key references [ROLES]([urid]) not null,
   primary key ([u_id], [ur_id])
);
GO