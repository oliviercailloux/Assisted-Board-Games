const database = {
    get: function(url, callback) {
        $.ajax({
            url,
            type: 'GET',
            crossDomain: true,
            async: false
        }).done(data => callback(data));
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