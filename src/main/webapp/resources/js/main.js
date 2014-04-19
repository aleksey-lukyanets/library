
//--------------------------------------------------- Глобальные переменные

// Адрес сервера
var serverUrl;

// Список стран
var countryTitleToId = new Object();

// Ассоциативный массив пар [имя автора]=[его идентификатор]
var authorMapNameToId = new Object();

//------------------------------------------------------------------------

function setServerUrl(url) {
    serverUrl = url;
}

/**
 * Подгрузка полных списков авторов и книг после завершения загруки страницы.
 */
function loadData() {
    loadAllAuthors();
    loadAllBooks();
    loadAllCountries();
}

function loadAllAuthors() {
    $("#authors").empty();
    $.ajax({
        type: "get",
        url: serverUrl + "/authors",
        success: function(data) {
            $.each(data, function(index, author) {
                authorMapNameToId[author.name] = author.id;
                addTableRow("authors", author.id, author.name + " [" + author.country + "]", "currentAuthor");
            });
            refreshSelectAccordingTo("authorSelect", authorMapNameToId);
        }
    });
}

function loadAllBooks() {
    $("#books").empty();
    $.ajax({
        type: "get",
        url: serverUrl + "/books",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(data) {
            $.each(data, function(index, book) {
                addTableRow("books", book.id, book.title, "bookUnit");
            });
        }
    });
    $('#allBooks').hide();
}

function loadAllCountries() {
    $.ajax({
        type: "get",
        url: serverUrl + "/countries",
        success: function(data) {
            $.each(data, function(index, country) {
                countryTitleToId[country.title] = country.id;
            });
            refreshSelectAccordingTo("countrySelect", countryTitleToId);
        }
    });
}

//------------------------------------------------ Запросы к списку авторов

/**
 * Добавление нового автора.
 * 
 * Добавление автора на страницу происходит без ожидания ответа на запрос.
 * Отдельно из ответа считывается идентификатор, присвоенный новому автору базой данных.
 *
 * Добавление автора с существующим именем не допускается.
 */
function addNewAuthor() {
    var authorName = document.getElementById("name").value;
    var select = document.getElementById("countrySelect");
    var countryTitle = select.options[select.selectedIndex].text;

    $.ajax({
        type: "post",
        url: serverUrl + "/authors",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        data: "{\"id\":0,\"name\":\"" + authorName + "\",\"country\":\"" + countryTitle + "\"}",
        statusCode: {
            201: function(data) {
                var authorTableRowIndex = addTableRow("authors", authorName, authorName + " [" + countryTitle + "]", "currentAuthor");

                // Добавление id автора в свойства его radiobutton, выбор этой radiobutton
                var authorRadio = $("input[name=currentAuthor][value=" + authorTableRowIndex + "]");
                authorRadio.prop('checked', true);
                var id = data["id"];
                authorRadio.attr('data-db-id', id);

                // Обновление выпадающего списка формы "Добавить книгу"
                authorMapNameToId[authorName] = authorId;
                refreshSelectAccordingTo("authorSelect", authorMapNameToId);
                $("#authorSelect").val(authorId);

                showRemoveAuthorAction();
                $("#books").empty();
                $('#allBooks').show();
            },
            406: function(data) {
                var string = interpretRestrictionsViolation(data);
                alert(string);
            },
            422: function() {
                alert("Добавление невозможно.\nАвтор с таким именем уже существует.");
            },
            500: function() {
                standardError500Alert();
            }
        }
    });
}

/**
 * Удаление автора.
 * 
 * Удаление автора с страницы происходит без ожидания ответа на запрос.
 */
function deleteAuthor()
{
    var checkedRadio = $('input[name=currentAuthor]:checked', '#authors');
    var authorId = $(checkedRadio).attr("data-db-id");
    $.ajax({
        type: "delete",
        url: serverUrl + "/authors/" + authorId,
        contentType: "application/json;charset=utf-8",
        statusCode: {
            200: function() {
                var rowIndex = $(checkedRadio).closest('tr').index();
                deleteTableRow("authors", rowIndex);
                loadAllBooks();

                for (var key in authorMapNameToId) {
                    if (authorMapNameToId[key] == authorId) {
                        delete authorMapNameToId[key];
                    }
                }
                refreshSelectAccordingTo("authorSelect", authorMapNameToId);
            },
            404: function() {
                alert("Удаление невозможно.\nЗапрошенный автор не существует.");
            },
            500: function() {
                standardError500Alert();
            }
        }
    });
}

//------------------------------------------------------- Запросы к списку книг

/**
 * Запрашивает и отображает список книг для заданного идентификатора автора.
 */
function getBooksForAuthorId(authorId) {
    $.ajax({
        type: "get",
        url: serverUrl + "/authors/" + authorId + "/books",
        dataType: "json",
        contentType: "application/json;charset=utf-8",
        statusCode: {
            200: function(data) {
                $('#allBooks').show();
                $("#books").empty();
                return $.each(data, function(index, book) {
                    addTableRow("books", book.id, book.title, "bookUnit");
                });
            },
            404: function(data) {
                alert("Запрошенный автор не существует.");
            },
            500: function() {
                standardError500Alert();
            }
        }
    });
}

/**
 * Добавление новой книги.
 * 
 * Добавление книги на страницу происходит без ожидания ответа на запрос.
 *
 * Добавление книги с существующим названием и именем автора не допускается,
 * но дубликат названия книги для другого автора разрешён.
 */
function addNewBook()
{
    var bookTitle = document.getElementById("title").value;
    var authorTitle = $("#authorSelect").children("option").filter(":selected").text();

    $.ajax({
        type: "post",
        url: serverUrl + "/books",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: "{\"id\":0,\"author\":\"" + authorTitle + "\",\"title\":\"" + bookTitle + "\"}",
        statusCode: {
            201: function(data) {
                // Очистка списка книг
                $("#books").empty();
                $('#authors input').removeAttr('checked');
                $("#deteleAuthorToggle").remove();

                var bookTableRowIndex = addTableRow("books", bookTitle, bookTitle, "bookUnit");
                var bookRadio = $("input[name=bookUnit][value=" + bookTableRowIndex + "]");
                bookRadio.attr('checked', true);

                var bookId = data["id"];
                bookRadio.attr('data-db-id', bookId);
                showRemoveBookAction();
            },
            406: function(data) {
                var string = interpretRestrictionsViolation(data);
                alert(string);
            },
            422: function() {
                alert("Добавление невозможно.\nУ автора уже существует книга с таким названием.");
            },
            500: function() {
                standardError500Alert();
            }
        }
    });
}

/**
 * Удаление книги.
 * 
 * Удаление книги со страницы происходит без ожидания ответа на запрос.
 */
function deleteBook()
{
    var checkedRadio = $('input[name=bookUnit]:checked', '#books');
    var bookId = $(checkedRadio).attr("data-db-id");
    $.ajax({
        type: "delete",
        url: serverUrl + "/books/" + bookId,
        contentType: "application/json;charset=utf-8",
        statusCode: {
            200: function() {
                var rowIndex = $(checkedRadio).closest('tr').index();
                deleteTableRow("books", rowIndex);
                reloadBooks();
            },
            404: function() {
                alert("Удаление невозможно.\nЗапрошенный автор не существует.");
            },
            500: function() {
                standardError500Alert();
            }
        }
    });
}

//---------------------------------------------- Функции обработки ответов Ajax

function interpretRestrictionsViolation(response) {
    var string = "Некорректные параметры запроса:";
    var json = jQuery.parseJSON(response.responseText);
    $(json.fieldErrors).each(function(index){
        string += "\n- поле \"" + this.field + "\": " + this.message;
    });
    return string;
}

function standardError500Alert() {
    alert("Что-то пошло не так.");
}

//--------------------------------------------- Операции со спискам ии кнопками

function authorRadioClicked() {
    var checkedRadio = $('input[name=currentAuthor]:checked', '#authors');
    var authorId = $(checkedRadio).attr("data-db-id");
    getBooksForAuthorId(authorId);
    $("#authorSelect").val(authorId);
    showRemoveAuthorAction();
}

function showRemoveAuthorAction() {
    var checkedRadio = $('input[name=currentAuthor]:checked', '#authors');
    var row = $(checkedRadio).closest('tr');
    addRemoveButton(row, "deteleAuthorToggle", "deleteAuthor()");
}

function showRemoveBookAction() {
    var checkedRadio = $('input[name=bookUnit]:checked', '#books');
    var row = $(checkedRadio).closest('tr');
    addRemoveButton(row, "deteleBookToggle", "deleteBook()");
}

function addRemoveButton(rowObject, buttonId, onClickAction) {
    $("#" + buttonId).remove();
    $(document.createElement("input")).attr({
        type: "button",
        id: buttonId,
        value: "удалить",
        onclick: onClickAction,
        class: "buttonRemove btn btn-danger"
    }).appendTo(rowObject);
}

function reloadBooks() {
    $("#books").empty();
    loadAllBooks();
    $('#authors input').removeAttr('checked');
    $("#deteleAuthorToggle").remove();
}

//-------------------- Вспомогательные функции для работы с элементами страницы

/**
 * Добавляет строку в таблицу.
 */
function addTableRow(tableId, dataTitle, rowText, groupName)
{
    var table = document.getElementById(tableId);
    var rowCount = table.rows.length;
    var row = table.insertRow(rowCount);
    var cell1 = row.insertCell(0);

    $(document.createElement("input")).attr({
        type: "radio",
        name: groupName,
        value: rowCount,
        'data-db-id': dataTitle
    }).appendTo(cell1);

    var text = document.createElement("name");
    var textNode = document.createTextNode(rowText);
    text.appendChild(textNode);
    cell1.appendChild(text);

    return rowCount;
}

/**
 * Удаляет строку из таблицы.
 */
function deleteTableRow(tableId, rowIndex)
{
    var table = document.getElementById(tableId);
    table.deleteRow(rowIndex);
}

/**
 * Обновляет элементы заданного выпадающего списка в соответствии
 * с заданным массивом текстовых меток.
 */
function refreshSelectAccordingTo(selectId, optionsMap) {
    var selbox = document.getElementById(selectId);
    $(selbox).empty();
    for (var key in optionsMap) {
        createOptionInSelect(selbox, key, optionsMap[key]);
    }
}

/**
 * Добавляет элемент с заданным текстом в заданный выпадающий список.
 */
function createOptionInSelect(selbox, txt, value) {
    selbox.options[selbox.options.length] = new Option(txt, value);
}
