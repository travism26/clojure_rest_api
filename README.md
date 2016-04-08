# clojure-rest
Decided to learn clojure, create a rest web app, and use reactJS for the UI.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen
[Postgresql]: https://wiki.postgresql.org/wiki/Detailed_installation_guides

## Setup Backend

   Run CLI commands:
   `createuser postgres` or whatever your username you want.
   `createdb -U postgres -p 5432 -E UTF8 -O postgres clojure_test` Replace postgres with user you created.
Note: tables will be created when lein starts up if your schema is not already created it will auto create it for you.

## Running

To start a web server for the application, run:

    lein ring server

    TEST URL: http://localhost:3000/api/travis/test
    ;#=> what's up buddy!
    
    API Call: http://localhost:3000/api/travis/
    ;#=> {:id "xues1-123sc2-fsasda3-2123s", :title "This is a title", :text "Im Back!"}
## Creating Dummy data
CLIs

`psql -U postgres -d clojure_test`
`insert into documents (id, title, text) VALUES ('xues1-123sc2-fsasda3-2123s','This is a title', 'Im Back!');`

## License

MIT
