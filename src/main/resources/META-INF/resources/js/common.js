const database = {
    get: function(url, callback) {
        $.ajax({
            url,
            type: 'GET',
            crossDomain: true,
            async: false,
            success : function(data) {
                callback(data);
            },
            error: function (xhr, resp, text){
                window.alert("La game n'existe pas ! changez d'ID"); 
            }
        })
    },

    post: function(url, data, type = '', callback) {
        const objSent = {
            url,
            type: 'POST',
            data,
            crossDomain: true,
            async: false
        };

        if (type === 'json') {
            objSent['contentType'] = 'application/json';
            objSent['dataType'] = 'json';
        }

        $.ajax(objSent).done(data => callback(data));
    }
}