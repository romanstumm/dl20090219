@echo off
SET JAVA_HOME=D:\jdk\jdk1.6.0_02
SET JAVA_CMD=start %JAVA_HOME%\bin\javaw.exe
rem SET JAVA_CMD=%JAVA_HOME%\bin\java.exe
rem SET JAVA_CMD=%JAVA_HOME%\bin\java -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=y
SET MYCP=config;lib\antlr-2.7.6.jar;lib\asm.jar;lib\asm-attrs.jar;lib\bsh-core-2.0b4.jar;lib\c3p0-0.9.1.jar
SET MYCP=%MYCP%;lib\cglib-2.1.3.jar;lib\commons-beanutils-core.jar;lib\commons-collections-2.1.1.jar
SET MYCP=%MYCP%;lib\commons-lang-2.3.jar;lib\commons-logging-1.0.4.jar;lib\concurrent-1.3.2.jar
SET MYCP=%MYCP%;lib\connector.jar;lib\dart-2.x.jar;lib\dartgui-2.x.jar;lib\dom4j-1.6.1.jar;lib\ehcache-1.2.3.jar
SET MYCP=%MYCP%;lib\hibernate3.jar;lib\jaas.jar;lib\javassist.jar;lib\jdbc2_0-stdext.jar;lib\jta.jar
SET MYCP=%MYCP%;lib\liquidlnf.jar;lib\log4j-1.2.11.jar;lib\nimrodlf.j16.jar;lib\postgresql-8.2-506.jdbc4.jar
SET MYCP=%MYCP%;lib\proxool-0.8.3.jar;lib\substance.jar;lib\jl1.0.jar;lib\dbfdriver.jar

%JAVA_CMD% -cp %MYCP% org.en.tealEye.guiMain.AppStartup