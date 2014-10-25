describe("item", function () {
    describe("validation", function () {
        var itemValidation, itemForm;

        beforeEach(function () {
            itemValidation = new ItemValidation(function () {});
        });

        it("returns true when there are no validation errors", function () {
            itemForm = { price: "1"};

            expect(itemValidation.validate(itemForm)).toBeTruthy();
        });

        it("returns false when there are no validation errors", function () {
            itemForm = { price: "aaa"};

            expect(itemValidation.validate(itemForm)).toBeFalsy();
        });

        it("displays errors when there are price validation errors", function () {
            spyOn(itemValidation, 'errorDisplayStrategy');
            itemForm = { price: "aaa"};

            itemValidation.validate(itemForm)

            expect(itemValidation.errorDisplayStrategy).toHaveBeenCalledWith("Price should be a positive decimal number only");
        });
    });
});
  
