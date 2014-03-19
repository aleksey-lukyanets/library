
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
    for (var key in authorMapNameToId) {
        if (key === authorName) {
            alert("Добавление автора невозможно.\nАвтор с таким именем уже существует.");
            return;
        }
    }
    var select = document.getElementById("countrySelect");
    var countryTitle = select.options[select.selectedIndex].text;
    var countryId = select.options[select.selectedIndex].value;
    var authorTableRowIndex = addTableRow("authors", authorName, authorName + " [" + countryTitle + "]", "currentAuthor");

    // Добавление id автора в свойства его radiobutton, выбор этой radiobutton
    var authorRadio = $("input[name=currentAuthor][value=" + authorTableRowIndex + "]");
    authorRadio.prop('checked', true);
    
    $.ajax({
        type: "post",
        url: serverUrl + "/authors",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: "{\"name\":\"" + authorName + "\",\"countryId\":\"" + countryId + "\"}",
        success: function(data) {
            if (data["status"] == "ok") {
                var response = data["response"];
                var authorId = response["id"];
                authorRadio.attr('data-db-id', authorId);
            } else {
                alert("Что-то пошло не так.\nАвтор не был добавлен в базу данных.");
            }
        }
    });
    showRemoveAuthorAction();

    // Обновление выпадающего списка формы "Добавить книгу"
    authorMapNameToId[authorName] = authorId;
    refreshSelectAccordingTo("authorSelect", authorMapNameToId);
    $("#authorSelect").val(authorId);
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
        contentType: "application/json; charset=utf-8"
    });
    var rowIndex = $(checkedRadio).closest('tr').index();
    deleteTableRow("authors", rowIndex);
    $("#books").empty();

    for (var key in authorMapNameToId) {
        if (authorMapNameToId[key] == authorId) {
            delete authorMapNameToId[key];
        }
    }
    refreshSelectAccordingTo("authorSelect", authorMapNameToId);
}

//--------------------------------------------------- Запросы к списку книг

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
    $("#"+buttonId).remove();
    $(document.createElement("input")).attr({
        type: "button",
        id: buttonId,
        value: "удалить",
        onclick: onClickAction,
        class: "buttonRemove btn btn-danger"
    }).appendTo(rowObject);
}

function reloadBooks() {
    loadAllBooks();
    $('#authors input').removeAttr('checked');
    $("#deteleAuthorToggle").remove();
}

/**
 * Запрашивает и отображает список книг для заданного идентификатора автора.
 */
function getBooksForAuthorId(authorId) {
    $.ajax({
        type: "get",
        url: serverUrl + "/books/authored/" + authorId,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function(data) {
            if (data["status"] == "ok") {
                var booksList = data["response"];

                $('#allBooks').show();
                $("#books").empty();
                return $.each(booksList, function(index, book) {
                    addTableRow("books", book.id, book.title, "bookUnit");
                });
            } else {
                alert("Что-то пошло не так.\nНе удалось получить книги из базы данных.");
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
    var authorId = document.getElementById("authorSelect").value;

    // Очистка списка книг
    $("#books").empty();
    $('#authors input').removeAttr('checked');
    $("#deteleAuthorToggle").remove();
    
    var bookTableRowIndex = addTableRow("books", bookTitle, bookTitle, "bookUnit");
    var bookRadio = $("input[name=bookUnit][value=" + bookTableRowIndex + "]");
    bookRadio.attr('checked', true);

    $.ajax({
        type: "post",
        url: serverUrl + "/books",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        data: "{\"title\":\"" + bookTitle + "\",\"authorId\":\"" + authorId + "\"}",
        success: function(data) {
            if (data["status"] == "ok") {
                var response = data["response"];
                var bookId = response["id"];
                bookRadio.attr('data-db-id', bookId);
            } else {
                alert("Что-то пошло не так.\nКнига не была добавлена в базу данных.");
            }
        }
    });
    showRemoveBookAction();
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
        contentType: "application/json; charset=utf-8"
    });
    var rowIndex = $(checkedRadio).closest('tr').index();
    deleteTableRow("books", rowIndex);
}

//---------------- Вспомогательные функции для работы с элементами страницы

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
