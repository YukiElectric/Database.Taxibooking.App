package com.example.demo1.daoGetSalary;

import com.example.demo1.database.JDBCUtil;
import com.example.demo1.model.StatictisModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class GetSalaryDAO {

    private static String formatCurrency(double value) {
        Locale vietnameseLocale = new Locale("vi", "VN");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(vietnameseLocale);
        symbols.setCurrencySymbol("");
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMinimumFractionDigits(0);
        return decimalFormat.format(value).trim();
    }

    public static GetSalaryDAO getInstance(){
        return new GetSalaryDAO();
    }

    public ArrayList<StatictisModel> GetSalary(){
        ArrayList<StatictisModel> result = new ArrayList<>();
        try{
            Connection cnt = JDBCUtil.getConnection();
            String sql = "SELECT * FROM salary";        //13
            PreparedStatement st = cnt.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String date = rs.getString("date");
                String supplementary = formatCurrency(rs.getDouble("supplementary"));
                String salary = formatCurrency(rs.getDouble("salary"));
                String earning = formatCurrency(rs.getDouble("earning"));
                StatictisModel temp = new StatictisModel(date,supplementary,salary,earning);
                result.add(temp);
            }
            JDBCUtil.closeConnection(cnt);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
