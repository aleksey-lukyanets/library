--
-- PostgreSQL database create
--

CREATE DATABASE testdb
    WITH OWNER tester
    TEMPLATE=template0
    ENCODING 'UTF8'
    LC_COLLATE = 'Russian, Russia'
    LC_CTYPE = 'Russian, Russia'
    CONNECTION LIMIT  -1;

            
            
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>sql-maven-plugin</artifactId>
                <version>1.5</version>

                <dependencies>
                    <!-- specify the dependent jdbc driver here -->
                    <dependency>
                        <groupId>postgresql</groupId>
                        <artifactId>postgresql</artifactId>
                        <version>9.1-901.jdbc4</version>
                    </dependency>
                </dependencies>

                <!-- common configuration shared by all executions -->
                <configuration>
                    <driver>${database.class}</driver>
                    <username>${database.username}</username>
                    <password>${database.password}</password>
                    <url>${database.url}</url>
                    <settingsKey>sensibleKey</settingsKey>
                    <!--all executions are ignored if -Dmaven.test.skip=true-->
                    <skip>${maven.test.skip}</skip>
                </configuration>

                <executions>
                    <execution>
                        <id>drop-db-before-test-if-any</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>execute</goal>
                        </goals>
                        <configuration>
                            <!-- need another database to drop the targeted one -->
                            <url>${database.url}</url>
                            <autocommit>true</autocommit>
                            <sqlCommand>drop database testdb</sqlCommand>
                            <!-- ignore error when database is not avaiable -->
                            <onError>continue</onError>
                        </configuration>
                    </execution>

                    <execution>
                        <id>create-db</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>execute</goal>
                        </goals>
                        <configuration>
                            <autocommit>true</autocommit>
                            <srcFiles>
                                <srcFile>src/test/resources/sql/create-db-testdb.sql</srcFile>
                            </srcFiles>
                        </configuration>
                    </execution>

                    <execution>
                        <id>create-data</id>
                        <phase>process-test-resources</phase>
                        <goals>
                            <goal>execute</goal>
                        </goals>
                        <configuration>
                            <orderFile>ascending</orderFile>
                            <fileset>
                                <basedir>${basedir}</basedir>
                                <includes>
                                    <include>src/test/resources/sql/testdb-data.sql</include>
                                </includes>
                            </fileset>
                        </configuration>
                    </execution>

                    <!-- drop db after test -->
                    <execution>
                        <id>drop-db-after-test</id>
                        <phase>test</phase>
                        <goals>
                            <goal>execute</goal>
                        </goals>
                        <configuration>
                            <autocommit>true</autocommit>
                            <sqlCommand>drop database testdb</sqlCommand>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
