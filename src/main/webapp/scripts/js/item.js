function ItemValidation(errorDisplayStrategy) {
    this.errorDisplayStrategy = errorDisplayStrategy;
}

ItemValidation.prototype.validate = function (itemForm) {
    var NUMERICAL_PATTERN = /^[0-9.]+$/;
    var isValid = true;

    if (!itemForm.price.match(NUMERICAL_PATTERN)) {
        this.errorDisplayStrategy("Price should be a positive decimal number only");
        isValid = false;
    }

    return isValid;
};

function validateForm() {
    var errorDisplayStrategy = function(error) { alert(error); };
    var itemValidation = new ItemValidation(errorDisplayStrategy);

    return itemValidation.validate(serializeObject(document.forms["addItem"]));
}

