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
                        <a href="<c:url value="/faces/index.xhtml" />" class="btn btn-xs btn-default">перейти к JSF-реализации</a>
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
                            см. <a href="<c:url value="/resources/js/main.js" />">main.js</a>) для асинхронного
                            обращения к серверу, все операции выполняются без обновления страницы.</p>
                        
                        <h4 class="modal-title">Технологии</h4>
                        
                        <p>Приложение разработано на Java с использованием технологий:</p>
                        <ul class="discharged">
                            <li>сервлет: Spring MVC, JavaServer Pages;</li>
                            <li>доступ к данным: Hibernate;</li>
                            <li>веб-интерфейс: jQuery, AJAX, Bootstrap;</li>
                            <li>база данных: PostgreSQL;</li>
                            <li>контейнер сервлетов: Apache Tomcat.</li>
                        </ul>
                        
                        <h4 class="modal-title">Веб-слой в стиле REST</h4>
                        
                        <p>Архитектура веб-слоя приложения реализована в соответствии со стилем REST.
                            Ниже приведены методы доступа к ресурсам библиотеки.</p>
                        <ul class="discharged list-unstyled">
                            <li>авторы:
                                <ul class="list-unstyled unstyled-inner">
                                    <li><code>GET /authors</code> — возвращает перечень авторов</li>
                                    <li><code>POST /authors</code> — добавляет нового автора и возвращает его id</li>
                                    <li><code>DELETE /authors/:authorId</code> — удаляет указанного автора</li>
                                </ul>
                            </li>
                            <li>книги:
                                <ul class="list-unstyled unstyled-inner">
                                    <li><code>GET /books</code> — возвращает перечень книг всех авторов</li>
                                    <li><code>GET /books/authored/:authorId</code> — возвращает список книг указанного автора</li>
                                    <li><code>POST /books</code> — добавляет новую книгу</li>
                                    <li><code>DELETE /books/:bookId</code> — удаляет указанную книгу</li>
                                </ul>
                            </li>
                            <li>страны:
                                <ul class="list-unstyled unstyled-inner">
                                    <li><code>GET /countries</code> — возвращает перечень стран</li>
                                </ul>
                            </li>
                        </ul>
                        <p>Результат всех GET-запросов доступен через веб-браузер. Так обращение
                            к ресурсу <code>/countries</code> возвращает список стран в формате JSON:</p>
                        <pre style="width: 50%;">[<br>    {"id": 1, "title": "Германия"},<br>    {"id": 2, "title": "Дания"},<br>    {"id": 3, "title": "Россия"},<br>    {"id": 4, "title": "США"}<br>]</pre>
                        <p>Запросы POST и DELETE можно выполнить любым REST-клиентом.</p>

                        <h4 class="modal-title">Обратная связь</h4>
                        
                        <p>Все запросы POST и DELETE возвращают информацию о результате их исполнения,
                            которая включается в объект-ответ JSON отдельным полем <code>status</code>
                            со значением <code>ok</code> или <code>error</code>.</p>
                        <p>Запрос на добавление нового автора может выглядеть так:</p>
                        <pre style="width: 55%;">{"name": "Лев Толстой", "countryId": 3}</pre>
                        <p>Ответ приложения при успешном добавлении автора:</p>
                        <pre style="width: 45%;">{<br>    "status": "ok",<br>    "response": {<br>        "id": 271,<br>        "name": "Лев Толстой",<br>        "country": "Россия"<br>    }<br>}</pre>

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
    </body>
</html>