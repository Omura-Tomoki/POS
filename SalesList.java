//-*- java -*-
/**********************************************************************************************************************
 *
 *  福岡大学工学部電子情報工学科プロジェクト型ソフトウェア開発演習教材
 *
 *  Copyright (C) 2015-2023 プロジェクト型ソフトウェア開発演習実施チーム
 *
 *  商品決済1件分に相当する商品販売の集合を表すクラス。
 *
 *********************************************************************************************************************/

package jp.ac.fukuoka_u.tl.NanakumaPOS;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;


/*
 *  商品決済1件分の商品販売の集合
 */

public class SalesList extends AbstractTableModel {
    /*
     *  内部データ
     */
    // 決済対象商品販売リスト
    private ArrayList<Sale> salesList = null;


    /*
     *  コンストラクタ
     */

    public SalesList() {
        salesList = new ArrayList<Sale>();
    }


    /*
     *  当該決済対象商品販売リストを空にする。
     */

    public void clear() {
        salesList.clear();
    }


    /*
     *  当該決済対象商品販売リスト内の商品販売の件数を返す。
     */

    public int getNumOfSales() {
        return salesList.size();
    }


    /*
     *  当該決済対象商品販売リストの ith 件目の商品販売を取得する。
     */

    public Sale getIthSale(int ith) {
        return salesList.get(ith);
    }


    /*
     *  当該決済対象商品販売リスト内の商品コード code の商品の販売を検索する。
     *  見つかった場合は当該販売リストにおける位置を，見つからなかった場合は -1 を返す。
     */

    public int find(String code) {
        for (int idx = 0; idx < getNumOfSales(); idx++) {
            if (salesList.get(idx).getArticleCode().equals(code)) {
                return idx;
            }
        }
        return -1;
    }


    /*
     *  当該決済対象商品販売リストに商品販売 sale を追加する。
     */

    public void add(Sale sale) {
        // 当該決済対象商品販売リストに追加済みの商品販売でない場合のみ追加する。
        if (find(sale.getArticleCode()) < 0) {
            salesList.add(sale);
        }
    }


    /*
     *  当該決済対象商品販売リストの商品販売の合計金額を返す。
     */

    public int getTotalPrice() {
        int totalPrice;

        totalPrice = 0;
        for (Sale sale: salesList) {
            totalPrice += sale.getSubTotalPrice();
        }
        return totalPrice;
    }


    /******************************************************************************************************************
     *  JTable とのインターフェースために使用されるメソッド群
     *  いずれも AbstractTableModel で定義されたものをオーバーライドしている。
     *****************************************************************************************************************/
    /*
     *  テーブルの行数を返す。
     */

    @Override
    public int getRowCount() {
        return getNumOfSales();
    }


    /*
     *  テーブルの桁数を返す。
     */

    @Override
    public int getColumnCount() {
        return 6;
    }


    /*
     *  テーブルの桁の名前を返す。
     */

    @Override
    public String getColumnName(int columnIndex) {
        final String[] columnName = new String[] {
                "購入日",
                "商品コード",
                "商品名",
                "販売単価",
                "販売個数",
                "小計",
        };
        return columnName[columnIndex];
    }


    /*
     *  テーブルの桁の型を返す。
     */

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        final Class<?>[] columnClass = new Class<?>[] {
                String.class,
                String.class,
                String.class,
                Integer.class,
                Integer.class,
                Integer.class,
        };
        return columnClass[columnIndex];
    }


    /*
     *  テーブルが編集可能か否かを返す。
     */

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


    /*
     * テーブルの rowIndex 行 columnIndex 桁のデータを返す。
     */

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object ret = null;
        Sale sale = salesList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                ret = sale.getSalesDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
                break;
            case 1:
                ret = sale.getArticleCode();
                break;
            case 2:
                ret = sale.getArticleName();
                break;
            case 3:
                ret = sale.getSalesPrice();
                break;
            case 4:
                ret = sale.getSalesQuantity();
                break;
            case 5:
                ret = sale.getSubTotalPrice();
                break;
        }
        return ret;
    }


    /*
     * テーブルの rowIndex 行 columnIndex 桁のデータを aValue に設定する。
     */

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Sale sale = salesList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                sale.setSalesDate((ZonedDateTime)aValue);
                break;
            case 1:
                sale.setArticleCode((String)aValue);
                break;
            case 2:
                sale.setArticleName((String)aValue);
                break;
            case 3:
                sale.setSalesPrice((int)aValue);
                break;
            case 4:
                sale.setSalesQuantity((int)aValue);
                break;
            case 5:
                break;
        }
    }
}
