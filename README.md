Reflection 1:

I have learned how to implement a basic CRUD but instead doing it in the django framework I did it
in the SpringBoot framework, where instead os using the MVT method I used the MVC structure. I applied
clean code principles by separating responsibilities the productController only handles HTTP endpoints and 
view models, the ProductService layer handle business logic, and the productRepo manages data storage operations.
This seperation makes the code easier to read, test and maintain because each class has a clear purpose. I also
used meaningful naming conventions for classes and endpoints examples /product/create and /product/list.

For secure coding practices, I avoided constructing URLs or HTML output using string
concatenation and used Thymeleaf for rendering values which helps prevent injection issues by escaping
output by default. The form submission uses POST for state changing actions while aligns with safer HTTP.
Improvements such as productQuantity should be an integer input and validated on the server side for example 
quantity must be non negative to prevent invalid data being stored. I should add input validation using @Valid 
and bean validation annotations in the model such as @NotBlank for productName, and then handle
validation errors in the controller to show feedback on the form and ofcourse the current repo stores
product in an in memory list so data will be lost on restart, this has to be replaced with a persistent storage
database. Overall the current structure is clean and works but adding validation, consistent naming
and better error handling would improve reliability and security I hope to keep learning SpringBoot especially 
the annotations im still unfamiliar with and previous java concepts.