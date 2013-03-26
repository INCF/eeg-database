Date.prototype.format = function (fmt) {
    var date = this;

    return fmt.replace(
        /\{([^}:]+)(?::(\d+))?\}/g,
        function (s, comp, pad) {
            var fn = date["get" + comp];

            if (fn) {
                var v = (fn.call(date) +
                    (/Month$/.test(comp) ? 1 : 0)).toString();

                return pad && (pad = pad - v.length)
                    ? new Array(pad + 1).join("0") + v
                    : v;
            } else {
                return s;
            }
        });
};
