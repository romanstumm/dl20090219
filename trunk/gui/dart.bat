@echo off
rem SET JAVA_HOME=D:\jdk\jdk1.6.0_02
rem SET JAVA_CMD=start %JAVA_HOME%\bin\javaw.exe
SET JAVA_CMD=java.exe
rem SET JAVA_CMD=%JAVA_HOME%\bin\java -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y
SET MYCP=config;lib\antlr-2.7.6.jar;lib\bsh-core-2.0b4.jar;lib\commons-beanutils-core.jar;lib\commons-net-2.2.jar
SET MYCP=%MYCP%;lib\commons-collections-3.2.1.jar;lib\commons-lang-2.4.jar;lib\commons-logging-1.1.1.jar
SET MYCP=%MYCP%;lib\dart-2.x.jar;lib\dartgui-2.x.jar;lib\dom4j-1.6.1.jar;lib\ehcache-1.2.3.jar
SET MYCP=%MYCP%;lib\freemarker-2.3.15.jar;lib\gluegen-rt.jar;lib\hibernate-cglib-repack-2.1_3.jar
SET MYCP=%MYCP%;lib\hibernate3.jar;lib\javassist-3.4.GA.jar;lib\jl1.0.jar;lib\jogl.jar;lib\jta-1.1.jar
SET MYCP=%MYCP%;lib\liquidlnf.jar;lib\log4j-1.2.15.jar;lib\nimrodlf.j16.jar;lib\postgresql-8.4-703.jdbc4.jar
SET MYCP=%MYCP%;lib\slf4j-api-1.5.6.jar;lib\slf4j-log4j12-1.5.6.jar;lib\xpp3_min-1.1.4c.jar;lib\xstream-1.3.1.jar
SET MYCP=%MYCP%;lib\substance.jar;lib\jl1.0.jar;lib\poi-3.2-FINAL-20081019.jar

%JAVA_CMD% -cp %MYCP% org.en.tealEye.guiMain.AppStartup