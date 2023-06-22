USE DB_SUPER
GO
   DELETE [dbo].[ACCOUNT];
   DBCC CHECKIDENT ('[ACCOUNT]', RESEED, 1000);
GO

INSERT INTO [dbo].[ACCOUNT]
   ([username], [email], [password], [fullname])
VALUES
   ('admin', 'ngoduyhoaname2002@gmail.com', 'admin', N'StuDev Courage')
GO
