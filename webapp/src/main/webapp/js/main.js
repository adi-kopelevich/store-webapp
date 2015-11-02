$(document).ready(function () {

    var items = [];
    loadList(items);
    getTaskListFromRemote();
    var index;

    // if input is empty disable button
    $('button').prop('disabled', true);
    $('input').keyup(function () {
        if ($(this).val().length !== 0) {
            $('button').prop('disabled', false);
        } else {
            $('button').prop('disabled', true);
        }
    });
    // bind input enter with button submit
    $('#main-input').keypress(function (e) {
        if (e.which === 13) {
            if ($('#main-input').val().length !== 0)
                $('#main-button').click();
        }
    });
    $('#main-button').click(function () {
        var value = $('#main-input').val();
        $('#main-input').val('');
        var item = createItemObject(value);
        saveItemToRemote(item);
        // set button to
        $('button').prop('disabled', true);
    });
    // delete one item
    $('ul').delegate("span", "click", function (event) {
        event.stopPropagation();
        index = $('span').index(this);
        $('li').eq(index).remove();
        deleteItemFromRemote(items[index])
    });

    // edit panel
    $('ul').delegate('li', 'click', function () {
        index = $('li').index(this);
        var content = items[index];
        $('#edit-input').val(content.name);
    });

    $('#edit-button').click(function () {
        items[index].name = $('#edit-input').val();
        updateItemToRemote(items[index]);
    });

    // loadList
    function loadList(items) {
        $('li').remove();
        if (items != null && items.length > 0) {
            for (var i = 0; i < items.length; i++) {
                $('ul').append('<li class= "list-group-item" data-toggle="modal" data-target="#editModal">' + items[i].name + '<span class="glyphicon glyphicon-remove"></span</li>');
            }
        }
    };

    function createItemObject(name) {
        var myItem = new Object();
        myItem.id = Math.floor((Math.random() * 1000) + 1);
        myItem.name = name;
        myItem.category = "home";
        // set a reminder to a week from now
        var aWeekFromNow = new Date();
        aWeekFromNow.setDate(aWeekFromNow.getDate()+7)
        myItem.reminder = aWeekFromNow.getTime(); // the time in millis (UTC)
        myItem.notes = ["note1", "note2"];
        return myItem;
    }

    function getTaskListFromRemote() {
        jQuery.ajax({
            type: "GET",
            url: "/rest/items",
            headers: {Accept: "application/json; charset=utf-8"},
            dataType: "json",
            success: function (data, status, jqXHR) {
                items = [];
                var retItems = data.items;
                if (retItems != null && retItems.length > 0) {
                    for (var i = 0; i < retItems.length; i++) {
                        items.push(retItems[i]);
                    }
                }
                loadList(items)
            },
            error: function (jqXHR, status) {
                console.log("Failed to retrieve list of items");
            }
        });
    }

    function saveItemToRemote(item) {
        jQuery.ajax({
            type: "POST",
            url: "/rest/items",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(item),
            dataType: "text",
            success: function (data, status, jqXHR) {
                getTaskListFromRemote();
            },
            error: function (jqXHR, status) {
                console.log("Failed to save item: " + item.name);
            }
        });
    }

    function deleteItemFromRemote(item) {
        jQuery.ajax({
            type: "DELETE",
            url: "/rest/items/" + item.id,
            contentType: "application/json; charset=utf-8",
            success: function (data, status, jqXHR) {
                getTaskListFromRemote();
            },
            error: function (jqXHR, status) {
                console.log("Failed to delete item: " + item);
            }
        });
    }

    function updateItemToRemote(item) {
        jQuery.ajax({
            type: "PUT",
            url: "/rest/items",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(item),
            dataType: "text",
            success: function (data, status, jqXHR) {
                getTaskListFromRemote();
            },
            error: function (jqXHR, status) {
                console.log("Failed to update item: " + item);
            }
        });
    }
});
