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

Reflection 2:

1. After writing the unit tests for Edit and Delete Product 
I felt more confident because the tests give immediate feedback that core behaviors still work after code changes
writing tests also made me realize testing through the browser manually is not enough, unit test help catches
regression early and make refactoring safer. There is no fixed number  of how many unit test the better guideline
is that each test should cover one behavior or scenario clearly
and the class should contain enough tests to cover the important paths normal flow positive, edge cases, 
and failure paths (negative). In practice, having multiple small focused tests is better than one large test 
that checks too many things at once because smaller tests are easier to debug and understand.
To make sure unit tests are enough I should identify the program’s important behaviors and risks
then ensure tests cover them for example common user actions, boundary values, and expected failures 
A helpful metric for this is code coverage, which shows how much of the code is executed by tests 
Coverage is useful to find untested areas, but it is not the same as correctness. 
Even if I achieved 100% coverage it does not guarantee there are no bug tests and can still miss logical errors, 
wrong assertions, missing edge cases, or incorrect requirements. For example, a test might run a line of code 
but not verify the output properly, so coverage is high but quality is low. 
Therefore, I should treat coverage as a guide to improve test completeness 
while still focusing on meaningful assertions, edge-case testing, and testing behaviors that matter to users.

2.If I create another functional test class by copying the same setup code such as
port, baseUrl building, and Selenium setup the code will still work but becomes less clean because it introduces 
duplication. This reduces maintainability if the setup changes URL format, configuration, timeouts, 
I must update multiple files and may forget one causing inconsistent tests. It can also reduce readability 
because repeated boilerplate hides the actual test intent, and encourages magic strings
(hard-coded URLs/selectors) scattered across classes.
To improve cleanliness, I should refactor shared setup into a base class 
example BaseFunctionalTest with @LocalServerPort, baseUrl, and @BeforeEach or extract common 
steps into helper methods. I should also standardize locators using stable IDs and reuse.
constants for common routes like /product/create and /product/list. 
This keeps tests consistent, readable, and easier to change as the test suite grows.
