Magdalena Sarzyńska - Automation Tests

##Lauch

In case of project lauch you need to :

### clone my repository and build/run project

* By enter in shell:
```js
git clone https://github.com/Madzia/org.automation.allegro.tests.git
cd org.automation.allegro.tests
```
* Load project into Eclipse IDE.
* Run AutomationTest.

##Test elements:

1. Open webpage 'www.allegro.pl'.
2. Enter 'smartwatch' into search field.
3. Click the magnifier button.
4. search results sorted by 'cena: od najwyższej'.
5. Click first auction on the list.
6. In item website verify that price is bigger than 1000 zł.
7. Open 'Dostawa i płatność'.
8. Download and write into log available supply options.

##Example tests output

https://test-output-example.herokuapp.com/

##Requirements

* Eclipse IDE (or another one, preferred version is Luna Service Release 2 (4.4.2) or newer). 
* Java SE 7 (or later).
* Maven (with Eclipse Marketplace synchronisation).
* TestNG (with Eclipse Marketplace synchronisation).
* Selenium (will be downloaded by Maven).
