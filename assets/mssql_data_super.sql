USE DB_SUPER
GO
   -- DELETE [dbo].[US_UR];
   -- DELETE [dbo].[US_UP];
   DELETE [dbo].[UACCOUNT];
   DELETE [dbo].[UROLES];
   DELETE [dbo].[UACCESS];
   DELETE [dbo].[UPLATFORM];
   DBCC CHECKIDENT ('[UACCOUNT]', RESEED, 1000);
GO

-- USER ROLES
INSERT INTO [dbo].[UROLES] 
   ([urid], [role])
VALUES 
   (0, N'USER'),
   (1, N'STAFF'),
   (2, N'ADMIN'),
   (3, N'PARTNER')
GO

-- OAUTH2 PLATFORM
INSERT INTO [dbo].[UPLATFORM] ([upid], [up_name], [up_other])
VALUES 
   (0, N'SYSTEM', null),
   (1, N'GOOGLE', null),
   (2, N'FACEBOOK', null)
GO

-- role active for set: user(0-1-2), staff(3), for admin (0-1-2-3-4)
INSERT INTO [dbo].[UACCESS] ([uaid], [ua_name])
VALUES 
   (0, N'AWAITING'),
   (1, N'LOCK'),
   (2, N'PRIVATE'),
   (3, N'PROTECTED'),
   (4, N'PUBLIC');
GO

-- INSERT UACCOUNT
SET IDENTITY_INSERT [dbo].[UACCOUNT] ON
INSERT INTO [dbo].[UACCOUNT]
   ([uid], [username], [email], [password], [fullname], [regDate], [ua_id])
VALUES
   (1001, 'admin', 'ngoduyhoaname2002@gmail.com', PWDENCRYPT('123'), N'Admin System Test', '2023-02-18 08:58:33', 4),
   (1002, 'staff', 'sdhoa1922@gmail.com', PWDENCRYPT('123'), N'Staff System Test', '2023-05-04 04:45:17', 4),
   (1003, 'user1', 'user1.studev1922@gmail.com', PWDENCRYPT('123'), N'User System Test', '2023-03-25 09:09:27', 2),
   (1004, 'user2', 'user2.studev1922@gmail.com', PWDENCRYPT('123'), N'User System Test', '2023-04-05 21:32:00', 0),
   (1005, 'partner', 'partner.studev1922@gmail.com', PWDENCRYPT('123'), N'Partner System Test', '2023-01-22 15:08:08', 3);
GO

SET IDENTITY_INSERT [dbo].[UACCOUNT] OFF

-- INSERT UACCOUNT'S IMAGES
INSERT INTO [dbo].[UIMAGE]
   ([u_id], [image])
VALUES
   (1001, 'admin1.png'),
   (1001, 'admin3.png'),
   (1002, 'staff1.png');
GO

-- INSERT USER'S AUTHORIZATION
INSERT INTO [dbo].[US_UR]
   ([u_id], [ur_id])
VALUES
   (1001, 0), (1001, 1), (1001, 2),
   (1002, 1),
   (1004, 0),
   (1005, 0);
GO

-- INSERT USER PLATFORM
INSERT INTO [dbo].[US_UP]
   ([u_id], [up_id])
VALUES
   (1001, 0), (1001, 1),
   (1002, 0),
   (1003, 0);
GO

USE master
