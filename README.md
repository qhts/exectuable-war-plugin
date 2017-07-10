# exectuable-war-plugin
war转可执行jar插件，使用java8编译,内置tomcat8.0.32

## 快速使用：
1.在pom文件中定义war.name变量：
>\<properties><br>
\<war.name>原有war包名称（不含.war后缀）</war.name><br>
\<properties><br>
 
2.引入插件
>\<plugin><br>
				\<groupId>com.newera.plugin\</groupId><br>
  			\<artifactId>exectuable-war-plugin\</artifactId><br>
        \<version>1.0.6\</version><br>
				\<executions><br>
					\<execution><br>
						\<phase>install\</phase><br>
						\<goals><br>
							\<goal>tomcatwrapper\</goal><br>
						\</goals><br>
					\</execution><br>
				\</executions><br>
				\<configuration><br>
          \<!-- 是否删除原来的war文件 --><br>
					\<deleteWar>false\</deleteWar><br>
					\<tomcatSettings>warName=${war.name}\</tomcatSettings><br>
				\</configuration><br>
\</plugin><br>

3.启动jar，第一个参数为端口，第二个参数为contextpath
>jave -jar {文件名}.jar 8080 ""
等待启动完成，即可访问：http://localhost:8080

## 进阶配置：
插件可以针对tomcat进行参数配置，以下为一段示例：
>\<tomcatSettings>warName=${war.name},useExecutor=true,executor.name=tomcatThreadPool
,executor.namePrefix=client-exec-,executor.maxThreads=1000,executor.minSpareThreads=50,
executor.maxIdleTime=60000,protocol.connectionTimeout=20000,connector.URIEncoding=UTF-8,connector.secure=true
\</tomcatSettings><br>

更多参数可以参考官方有关Http11NioProtocol、Connector及StandardServer的javadoc：<br>
http://tomcat.apache.org/tomcat-8.0-doc/api/index.html
