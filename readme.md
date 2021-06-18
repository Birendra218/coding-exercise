# Stock Watcher

Stock watcher is a spring-boot rest application that lets users add stock to their watch list and let the user update the list as needed.


## How-to-start

Recommended Runtime environment for this application is java 11. Since Maven is used as a dependency management and build tool, the development machine requires Maven installation as well. This application is tested in java 11 and Maven 3.2.x.


use maven target as below to run this application from the root of the projct

```
mvn spring-boot:run
```

## Usage

For simplicity, this application does not have its context name and listens for HTTP requests on port 8080 as default in any spring-boot web app.

Adding, deleting, and retrieving stocks are available on same endpoint with proper HTTP verb as below:

```
localhost:8080/stocks/{stockName}

```

If HTTP verb GET is used, then the API is the above endpoint will return the stock detail for the stock name. If the stock is not already available in the watch list, expect an error message on the console/client.

If HTTP POST is used, the API will retrieve stock details from Yahoo Finance and add the stock to the watch list. Adding the same stock multiple times won't impact the functionality. For invalid stocks, a proper error message will be sent back to the client.

If HTTP DELETE is used on the same endpoint above, the stock will be removed from the watch list. If the stock is not on a watchlist, an error message is relayed back to the client.

