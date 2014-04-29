<!DOCTYPE html>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Библиотека — Spring MVC</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" media="screen"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/customization.css" media="screen"/>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/main.js" type="text/javascript" charset="utf-8"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js" type="text/javascript" charset="utf-8"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                setServerUrl($("body").attr('data-server-url'));
                loadData();
            });
        </script>
    </head>
    <body data-server-url="${serverUrl}">
        <%--
            Интерфейс пользователя.
        --%>
        <div align="center">
            <div id="bodyfield" align="left">
                <div class="clearfix" style="margin: 0 0 20px -10px;">
                    <div class="btn-group">
                        <span class="btn btn-xs btn-default disabled">сервлет Spring MVC</span>
                        <a href="${ejbVersion}" class="btn btn-xs btn-default">перейти к реализации EJB+JSF</a>
                    </div>
                    <div class="btn-group pull-right">
                        <a id="details" class="btn btn-danger">Как это работает?</a>
                    </div>
                </div>
                <table width="100%">
                    <tr>
                        <td width="40%"><h2>Авторы:</h2></td>
                        <td width="60%"><h2>Книги:</h2></td>
                    </tr>
                    <tr>
                        <td valign="top"><table class="units-list" id="authors"></table></td>
                        <td valign="top"><table class="units-list" id="books"></table></td>
                    </tr>
                    <tr>
                        <td>
                            <input type="button" id="btnAddAuthor" class="btn btn-default" value="добавить автора" />
                        </td>
                        <td>
                            <input type="button" id="allBooks" class="btn btn-primary btn-pad-right" onclick="reloadBooks()" value="показать все книги" />
                            <input type="button" id="btnAddBook" class="btn btn-default" value="добавить книгу" />
                        </td>
                    </tr>
                </table>
                <div class="footer">
                    <ul class="list-inline">
                        <li><a class="rest btn btn-xs btn-default">REST API</a></li>
                        <li><a href="https://github.com/aleksej-lukyanets/library">исходные коды на github</a></li>
                        <li><a href="http://spb.hh.ru/resume/002aa1acff01bbb6c90039ed1f744c5a305658">резюме на hh.ru</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            /* Клик по кнопке "показать все книги". */
            $('body').delegate('#allBooks', 'click', function() {
                $('#allBooks').hide();
            });
            /* Событие выбора автора. */
            $('body').delegate('input[name=currentAuthor]:checked', 'change', function() {
                authorRadioClicked();
            });
            /* Событие выбора книги. */
            $('body').delegate('input[name=bookUnit]:checked', 'change', function() {
                showRemoveBookAction();
            });
        </script>
        
        <%--
            Окна добавления авторов и книг.
        --%>
        <div id="addAuthorModal" class="modal bs-example-modal-sm modal-bottom-fixed" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
            <div class="modal-dialog modal-sm" style="line-height:160%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4>Добавление автора</h4>
                    </div>
                    <div class="modal-body">
                        <form class="addAuthor form-horizontal" style="text-align: left;">
                            <div class="form-group">
                                <label for="name" class="col-sm-5 control-label">имя автора:</label>
                                <div class="col-sm-7">
                                    <input type="text" name="name" id="name" class="form-control" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="countrySelect" class="col-sm-5 control-label">страна&nbsp;рождения:</label>
                                <div class="col-sm-7">
                                    <select name="countrySelect" id="countrySelect" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <center>
                                    <input type="button" class="btn btn-success" data-dismiss="modal" onclick="addNewAuthor()" value="добавить" />
                                </center>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <div id="addBookModal" class="modal bs-example-modal-sm modal-bottom-fixed" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
            <div class="modal-dialog modal-sm" style="line-height:160%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4>Добавление книги</h4>
                    </div>
                    <div class="modal-body">
                        <form class="addBook form-horizontal">
                            <div class="form-group">
                                <label for="name" class="col-sm-5 control-label">автор книги:</label>
                                <div class="col-sm-7">
                                    <select name="authorSelect" id="authorSelect" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="countrySelect" class="col-sm-5 control-label">название&nbsp;книги:</label>
                                <div class="col-sm-7">
                                    <input type="text" name="title" id="title" class="form-control" />
                                </div>
                            </div>
                            <div class="form-group">
                                <center>
                                    <input type="button" class="btn btn-success" data-dismiss="modal" onclick="addNewBook()" value="добавить" />
                                </center>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $('body').delegate('#btnAddAuthor', 'click', function() {
                $('#addAuthorModal').modal({show: true});
            });
            $('body').delegate('#btnAddBook', 'click', function() {
                $('#addBookModal').modal({show: true});
            });
        </script>
        
        <%--
            Окно "Как всё работает?"
        --%>
        <div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
            <div class="modal-dialog" style="line-height:160%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4>Добро пожаловать в веб-библиотеку (Spring MVC)</h4>
                    </div>
                    <div class="modal-body">
                        <p>Библиотека — простое клиент-серверное приложение, которое позволяет хранить,
                            просматривать, изменять списки авторов и книг в базе данных.
                            Благодаря применению JavaScript (jQuery, AJAX, JSON,
                            см. <a href="https://github.com/aleksej-lukyanets/library/blob/master/webapp/resources/js/main.js">main.js</a>)
                            для асинхронного обращения к серверу, все операции выполняются без
                            обновления страницы.</p>
                        
                        <h4 class="modal-title">Технологии</h4>
                        
                        <p>Приложение разработано на Java с использованием технологий:</p>
                        <ul class="discharged">
                            <li>сервлет: Spring MVC, JavaServer Pages;</li>
                            <li>доступ к данным и валидация: JPA, Hibernate;</li>
                            <li>тесты: Spring Test, JUnit, Mockito, Hamcrest, JSONPath;</li>
                            <li>веб-интерфейс: jQuery, AJAX, Bootstrap;</li>
                            <li>база данных: PostgreSQL;</li>
                            <li>контейнер сервлетов: Apache Tomcat.</li>
                        </ul>
                        
                        <h4 class="modal-title">REST-интерфейс приложения</h4>
                        
                        <p>Архитектура веб-слоя приложения реализована в соответствии со стилем REST.
                            Описание методов доступа приведено <a data-dismiss="modal" class="rest">на отдельной странице</a>.</p>

                        <h4 class="modal-title">Обработка исключений</h4>
                        
                        <p>Исключительные ситуации, возникающие в приложении, обрабатываются через создание,
                            трансляцию и обработку исключений. Обработка пользовательских исключений вынесена в единый класс
                            <code>library.exception.RestExceptionHandler</code>, где в ответ сервера добавлются
                            необходимые данные и статусы (включая ясные для восприятия описания ошибок валидации
                            объекта, полученного с запросом POST).</p>
                        <p>Работа с необрабатываемыми исключениями доверена классу
                            <code>SimpleMappingExceptionResolver</code> Spring, который возвращает клиенту
                            статус 500 Internal Server Error при возникновении такого рода исключений.</p>

                        <h4 class="modal-title">Тесты</h4>
                        
                        <p>Приложение включает 18 модульных тестов для классов-контроллеров и 20 интеграционных
                            тестов, работающих с тестовым набором данных из БД. Сравнение ответов сервера
                            производится средствами Hamcrest и JSONPath, в модульных тестах используются
                            объекты-заглушки Mockito.</p>

                        <h4 class="modal-title">Модель базы данных</h4>
                        
                        <center>
                            <img src="${pageContext.request.contextPath}${initParam.imagesPath}db-model.png"
                                 alt="схема базы данных"/>
                        </center>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $('body').delegate('#details', 'click', function() {
                $('#detailsModal').modal({show: true});
            });
        </script>
        
        <%--
            Окно "REST API"
        --%>
        <div id="restModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
            <div class="modal-dialog" style="line-height:160%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4>REST-интерфейс приложения</h4>
                    </div>
                    <div class="modal-body">
                        <p>REST-интерфейс предоставляет доступ к ресурсам приложения, обмен данными производится
                            в формате JSON. Для наглядности некоторые поля объектов (страна у автора, автор у книги)
                            используют текстовые строки вместо идентификаторов.</p>
                        
                        <p>Результат всех GET-запросов доступен для просмотра через веб-браузер. Так обращение
                            к ресурсу <code>/countries</code> возвращает список стран:</p>
                        <pre style="width: 50%;">[<br>    {"id": 1, "title": "Германия"},<br>    {"id": 2, "title": "Дания"},<br>    {"id": 3, "title": "Россия"},<br>    {"id": 4, "title": "США"}<br>]</pre>
                        <p>Запросы POST и DELETE можно выполнить любым REST-клиентом.</p>

                        <h4 class="modal-title">Обратная связь</h4>
                        
                        <p>Все запросы возвращают в заголовке ответа информацию о результате их исполнения.</p>
                        <p>Запрос на добавление нового автора может выглядеть так:</p>
                        <pre style="width: 90%;">заголовок:   Content-Type: application/json; charset=UTF-8<br>тело:        {"id": 0, "name": "Лев Толстой", "country": "Россия"}</pre>
                        <p>Ответ приложения:</p>
                        <pre style="width: 90%;">заголовок:   Status Code: 201 Created<br>             Content-Type: application/json; charset=UTF-8<br>             Location: http://library.jelasticloud.com/authors/8<br>тело:        {"id": 8, "name": "Лев Толстой", "country": "Россия"}</pre>
                        <p>Запрос на добавление книги с некорректным именем автора:</p>
                        <pre style="width: 90%;">заголовок:   Content-Type: application/json; charset=UTF-8<br>тело:        {"id": 0, "author": "абв", "title": "Анна Каренина"}</pre>
                        <p>Ответ приложения:</p>
                        <pre style="width: 95%;">заголовок:   Status Code: 406 Not Acceptable<br>             Content-Type: application/json; charset=UTF-8<br>тело:        {<br>                 "fieldErrors":<br>                 [{"field": "author", "message": "Неизвестный автор."}]<br>             }</pre>
                        
                        <h4 class="modal-title">Операции с ресурсами</h4>
                        
                        <h5>Авторы</h5>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th width="150">ресурс</th>
                                    <th>описание</th>
                                    <th>статусы ответа</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>GET /authors</td>
                                    <td>Возвращает перечень авторов.</td>
                                    <td>200</td>
                                </tr>
                                <tr>
                                    <td>GET /authors/:id</td>
                                    <td>Возвращает автора с указанным id.</td>
                                    <td>200 — автор возвращён в теле ответа,<br>
                                        404 — автора с указанным id не существует</td>
                                </tr>
                                <tr>
                                    <td>GET /authors/:id/books</td>
                                    <td>Возвращает список книг автора с указанным id.</td>
                                    <td>200 — книги возвращены в теле ответа,<br>
                                        404 — автора с указанным id не существует</td>
                                </tr>
                                <tr>
                                    <td>POST /authors</td>
                                    <td>Добавляет нового автора. В теле ответа возвращает созданного автора, в заголовке - ссылку на созданный ресурс.</td>
                                    <td>201 — автор создан,<br>
                                        406 — автор с таким именем уже существует,<br>
                                        422 — некорректные параметры автора (длина имени или названия страны)</td>
                                </tr>
                                <tr>
                                    <td>DELETE /authors/:id</td>
                                    <td>Удаляет автора с указанным id и возвращает его в теле ответа.</td>
                                    <td>200 — автор удалён,<br>
                                        404 — автора с таким id не существует</td>
                                </tr>
                            </tbody>
                        </table>
                        
                        <h5>Книги</h5>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th width="150">ресурс</th>
                                    <th>описание</th>
                                    <th>статусы ответа</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>GET /books</td>
                                    <td>Возвращает перечень книг всех авторов.</td>
                                    <td>200</td>
                                </tr>
                                <tr>
                                    <td>GET /books/:id</td>
                                    <td>Возвращает перечень книг всех авторов.</td>
                                    <td>200 — книга возвращена в теле ответа,<br>
                                        404 — книги с таким id не существует</td>
                                </tr>
                                <tr>
                                    <td>POST /books</td>
                                    <td>Добавляет новую книгу. В теле ответа возвращает созданную книгу, в заголовке - ссылку на созданный ресурс.</td>
                                    <td>201 — книга создана,<br>
                                        406 — книга с таким названием уже существует у указанного автора,<br>
                                        422 — некорректные параметры книги (длина имени автора или названия книги)</td>
                                </tr>
                                <tr>
                                    <td>DELETE /books/:id</td>
                                    <td>Удаляет книгу с указанным id и возвращает её в теле ответа.</td>
                                    <td>200 — книга удалена,<br>
                                        404 — книги с таким id не существует</td>
                                </tr>
                            </tbody>
                        </table>
                        
                        <h5>Страны</h5>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th width="150">ресурс</th>
                                    <th>описание</th>
                                    <th>статусы ответа</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>GET /countries</td>
                                    <td>Возвращает перечень стран.</td>
                                    <td>200</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            $('body').delegate('.rest', 'click', function() {
                $('#restModal').modal({show: true});
            });
        </script>
    </body>
</html>