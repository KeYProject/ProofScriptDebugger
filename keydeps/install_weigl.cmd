set L=lib/components/
set PATH=%PATH%;C:\Users\weigl\Downloads\apache-maven-3.5.2\bin

call mvn install:install-file -Dfile=%L%/key.core.jar -DgroupId=key-project-psdbg -DartifactId=key.core -Dversion=2.7-SNAPSHOT -Dpackaging=jar

call mvn install:install-file -Dfile=%L%/key.ui.jar -DgroupId=key-project-psdbg -DartifactId=key.ui -Dversion=2.7-SNAPSHOT -Dpackaging=jar

call mvn install:install-file -Dfile=%L%/key.util.jar -DgroupId=key-project-psdbg -DartifactId=key.util -Dversion=2.7-SNAPSHOT -Dpackaging=jar

call mvn install:install-file -Dfile=%L%/../libs/recoderKey.jar -DgroupId=key-project-psdbg -DartifactId=recoder -Dversion=2.7 -Dpackaging=jar