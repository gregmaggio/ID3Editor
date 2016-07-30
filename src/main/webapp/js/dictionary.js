

var Dictionary = function() {
    this._keys = new Array();
}

Dictionary.prototype.containsKey = function (key) {
    key = key.toString().toLowerCase();
    if (this[key] || (this[key] == '')) {
        return true;
    }
    return false;
}

Dictionary.prototype.add = function (key, value) {
    key = key.toString().toLowerCase();
    if (!this[key] && (this[key] != '')) {
        this[key] = value;
        this._keys[this._keys.length] = key;
    }
}

Dictionary.prototype.remove = function (key) {
    key = key.toString().toLowerCase();
    if (this[key] || (this[key] == '')) {
        this[key] = null;
        for (var index = 0; index < this._keys.length; index++) {
            if (this._keys[index].toLowerCase() == key.toLowerCase()) {
                this._keys.remove(index);
                break;
            }
        }
    }
}

Dictionary.prototype.getItem = function (key) {
    key = key.toString().toLowerCase();
    if (this[key] || (this[key] == '')) {
        return this[key];
    }
    return null;
}

Dictionary.prototype.setItem = function (key, value) {
    key = key.toString().toLowerCase();
    if (this[key] || (this[key] == '')) {
        this[key] = value;
    } else {
        this[key] = value;
        this._keys[this._keys.length] = key;
    }
    return null;
}


Dictionary.prototype.getKeys = function() {
    return this._keys;
}

Dictionary.prototype.size = function () {
    return this._keys.length;
}

Dictionary.prototype.toArray = function() {
    var items = new Array();
    for (var index = 0; index < this._keys.length; index++) {
        var key = this._keys[index];
        var value = this[key];
        items[items.length] = value;
    }
    return items;
}
