package com.knosoftware.apps.foodorganizer.database.mysql

import android.content.Context
import com.knosoftware.apps.foodorganizer.database.utils.DatabaseConnection
import org.json.JSONArray

/**
 * This class builds a query string for database selections
 * based on method based query language format
 * Created by michaelknofler on 25.11.17.
 */
open class MYSQLBuilder(context: Context, file: String) : DatabaseConnectionSupport, DbTableHandler {

    /**
     * Order directions
     */
    val ASC: Int = 1 // -- Ascending
    val DESC: Int = 2 // --  Descending
    protected val ASCENDING: String = " ASC"
    protected val DESCENDING: String = " DESC"
    private val DESTINCT: String = "DESTINCT "
    private val ORDER_BY: String = "ORDER BY "
    private val GROUP_BY: String = "GROUP BY "
    private val LIMIT: String = "LIMIT "
    private val FROM_TABLE: String = "FROM TABLE "
    private val SELECT: String = "SELECT "

    /**
     * This is the actual selection string
     */
    protected var selection: String = SELECT
    protected lateinit var table: String
    protected lateinit var where: String
    protected lateinit var column: String
    protected lateinit var ord_asc: String
    protected lateinit var ord_desc: String
    protected lateinit var group: String
    protected lateinit var limit: String


    protected lateinit var wheres: Array<String>
    protected lateinit var groups: Array<String>
    protected lateinit var order_asc: Array<String>
    protected lateinit var order_desc: Array<String>
    protected lateinit var columns: Array<String>
    protected lateinit var signs: Array<String>

    private var


    /**
     * equality signs
     */
    val EQ: String = "="
    val NE: String = "!="
    val GT: String = ">"
    val LT: String = "<"
    val LEQ: String = "<="
    val GEQ: String = ">="

    // -- for faster code checks
    val SIGNS: Array<String> = arrayOf(EQ, NE, GT, LT, LEQ, GEQ)

    /**
     * Counters for selection criteria
     */
    val TEXT: Pair<Int, String> = Pair<Int, String>(0, "TEXT")
    val REAL: Pair<Int, String> = Pair<Int, String>(1, "REAL")
    val BLOB: Pair<Int, String> = Pair<Int, String>(2, "BLOB")
    val INT: Pair<Int, String> = Pair<Int, String>(3, "INTEGER")

    override fun where_sign(wheres: Array<Pair<String, String>>): Any {
        // -- check sign
        for (w in wheres) {
            if (w.second in signs) {

            } else {
                // throw UnvalidSignException
            }
        }
        return this
    }

    override fun select(columns: Array<String>): MYSQLBuilder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        // -- add columns to collective string
        for (col in columns) {
            add_column(col)
        }

        return this
    }

    override fun select_where(column_value_pair: Array<Pair<String, String>>): MYSQLBuilder {


        // add where clause with corresponding value
        for (p in column_value_pair) {
            add_where(p.first)
            add_sign()
            add_column(p.second)
        }

        return this
    }

    override fun where(wheres: Array<String>): Any {

        for (where in wheres) {
            add_where(where)
        }

        return this
    }

    override fun where(where: String): Any {
        this.where.plus(where)
        return this
    }

    override fun from(table: String): Any {

        if (table.isBlank()) {
            this.table = FROM_TABLE + table
        } else {

        }
        return this
    }

    override fun order_by_asc(columns: Array<String>): Any {
        for (col in columns) add_asc(col)
        return this
    }

    override fun order_by_desc(columns: Array<String>): Any {

        for (col in columns) add_desc(col)
        return this
    }

    override fun group_by(group: Array<String>): Any {

        if (groups.size > 0) {
            groups.plus(", ")
        }

        groups.plus(group)

        return this
    }

    override fun order_by_asc(columns: String): Any {
        this.ord_asc.plus(columns)
        return this
    }

    override fun order_by_desc(columns: String): Any {
        this.ord_desc.plus(columns)
        return this
    }

    override fun group_by(group: String): Any {
        this.group.plus(group)
        return this
    }

    override fun limit_to(limit: Int): Any {
        this.limit = LIMIT + limit.toString()
        return this
    }

    override fun destinct(): Any {

        selection.plus(DESTINCT)

        return this
    }

    override fun call_query(): JSONArray? {

        if (check_data()) {
            // --  translate to post value format
            translate()

            super.query(selection)

        }
        return json_data
    }

    /**
     * add where clause for corresponding column
     */
    private fun add_where(name: String, sign: String = EQ) {

        if (wheres.size > 0) {
            where.plus(", ")
        }

        add_sign(sign)
        where.plus(name)

    }

    /**
     * Add column value
     */
    private fun add_column(column: String) {

        if (columns.size > 0) {
            columns.plus(", ")
        }

        columns.plus(column)
    }

    /**
     * Add an column which gets sorted ascending
     */
    private fun add_asc(column: String) {
        if (order_asc.size > 0) {
            order_asc.plus(", ")
        }

        order_asc.plus(column)
    }

    /**
     * Add an column which gets sorted descending
     */
    private fun add_desc(column: String) {
        if (order_desc.size > 0) {
            order_desc.plus(", ")
        }

        order_desc.plus(column)
    }

    /**
     * adds the sign to the corresponding key value pair
     * to make it definable
     */
    private fun add_sign(sign: String = EQ) {
        signs.plus(sign)
    }

    /**
     * Adds a group  to the group string
     */
    private fun add_group(group: String) {

        if (groups.size > 0) {
            group.plus(", ")
        } else {
            this.group = GROUP_BY + group
        }
    }

    /**
     * Translates the built string to the required
     * posting json_data
     */
    private fun translate() {

        // --  TODO build selection string
        if (!ord_asc.isBlank()) {
            ord_asc.plus(ASCENDING)

            ord_asc = ORDER_BY + ord_asc
        }
        if (!ord_desc.isBlank()) {

            ord_desc.plus(DESCENDING)

            /*
            assume that statement is already set, otherwise
            its empty and the ORDER BY is set here
             */
            if (ord_asc.isBlank()) {
                ord_desc = ORDER_BY + ord_desc
            }
        }

        // selection must contain SELECT ... already
        if (!selection.isBlank()) {
            selection.plus(
                    column +
                            table +
                            where +
                            ord_asc + // -- maybe empty
                            ord_desc + // -- maybe empty
                            group + // -- maybe empty
                            limit  // -- maybe empty
            )
        }
    }

    /**
     * Checks all json_data (Counter, liste) for correctness
     * @return check_ok
     */
    private fun check_data(): Boolean {
        var check_ok: Boolean = true

        var col_len = columns.size
        var where_len = wheres.size
        var signs_len = signs.size

        if (col_len == where_len) {

            // -- columns and wheres are ok
            if (col_len == signs_len) {
                // signs are also ok
            } else {
                // TODO --  missing sign
                check_ok = false
            }

        } else {

            check_ok = false

            // -- log message for missing value
            if (col_len < where_len) {
                // TODO -- missing column
            } else {
                // TODO -- missing value
            }
        }
        return check_ok
    }


}
