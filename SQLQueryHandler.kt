package // --package

import org.json.JSONArray

/**
 * Created by michaelknofler on 25.11.17.
 */
interface SQLQueryHandler {



    /**
     * Prepare select
     */
    fun select(columns:Array<String>): Any


    /**
     * Prepare select and where clause
     */
    fun select_where( column_value_pair:Array<Pair<String, String>>) : Any

    /**
     * Where clauses. If using only String parameter function
     * it will use "equal" for every entry
     * if other signs are required use @see <where(wheres:Array<Pair<String, String>>)>
     */
    fun where(wheres:Array<String>):Any

    /**
     * Uses a direct string of where clauses.
     */
    fun where(where:String):Any
    /**
     * Adds where clauses with different signs = , !=, ...
     */
    fun where_sign(wheres:Array<Pair<String,String>>):Any

    /**
     * Performs the actual Database query
     */
    fun call_query():JSONArray?

    /**
     * choose the table
     */
    fun from(table:String):Any

    /**
     * set Orders ascending
     */
    fun order_by_asc(columns:Array<String>):Any

    /**
     * set orders descending
     */
    fun order_by_desc(columns:Array<String>):Any

    /**
     * Set grouping
     */
    fun group_by(group:Array<String>):Any

    /**
     * set Orders ascending
     */
    fun order_by_asc(columns:String):Any

    /**
     * set orders descending
     */
    fun order_by_desc(columns:String):Any

    /**
     * Set grouping
     */
    fun group_by(group:String):Any

    /**
     * Define maximum amount of returned lines from result
     */
    fun limit_to(limit:Int):Any

    /**
     * SELECT DISTINCT statement is used to return only distinct (different) values
     */
    fun destinct():Any
}
