import knex from "knex";

const db = knex({
  client: "mysql",
  connection: {
    host: "127.0.0.1",
    user: "root",
    password: "",
    port: 3306,
    database: "database"
  }
})

export default db;
