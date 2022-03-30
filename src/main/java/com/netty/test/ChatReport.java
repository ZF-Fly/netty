package com.netty.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netty.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

public class ChatReport {

    class Chat implements Serializable {
        private String name;

        private JSONArray con;

        Chat(String de) {
            name = de;
            con = new JSONArray();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public JSONArray getCon() {
            return con;
        }

        public void setCon(JSONArray con) {
            this.con = con;
        }
    }

    public Map<Chat, List<Integer>> CHAT = new HashMap() {{
        put(new Chat("sophia"), new ArrayList<Integer>() {{
            add(639);
            add(909);
        }});
        put(new Chat("fiona"), new ArrayList<Integer>() {{
            add(1171);
            add(1228);
        }});
        put(new Chat("ivy"), new ArrayList<Integer>() {{
            add(1173);
            add(1230);
        }});
        put(new Chat("Criss"), new ArrayList<Integer>() {{
            add(2806);
            add(2818);
        }});
        put(new Chat("Stella"), new ArrayList<Integer>() {{
            add(2813);
            add(2820);
        }});
        put(new Chat("Nicole"), new ArrayList<Integer>() {{
            add(2814);
            add(2821);
        }});
        put(new Chat("Xena"), new ArrayList<Integer>() {{
            add(2815);
            add(2822);
        }});
        put(new Chat("Nancy"), new ArrayList<Integer>() {{
            add(3233);
        }});
        put(new Chat("Poseidon"), new ArrayList<Integer>() {{
            add(4579);
        }});
        put(new Chat("Dora"), new ArrayList<Integer>() {{
            add(110299);
        }});
        put(new Chat("Mia"), new ArrayList<Integer>() {{
            add(110460);
        }});
        put(new Chat("Louis"), new ArrayList<Integer>() {{
            add(2976);
        }});
        put(new Chat("Shea"), new ArrayList<Integer>() {{
            add(110569);
        }});
    }};

    @Test
    public void doReport() {

        String fileName = "C:\\Users\\Tyler\\Desktop\\3-15aaaa.xlsx";
        File file = new File(fileName);
        InputStream in;
        {
            try {
                JSONObject obj = new JSONObject();

                in = new FileInputStream(file);
                JSONArray array = FileUtil.readXlsx(in, true);
                Set<Chat> chats = CHAT.keySet();
                Chat activeChat = null;
                for (Object object : array) {
                    JSONObject itemJson = (JSONObject) object;
                    String applicationNo = itemJson.getString("loanNo");
                    String section = itemJson.getString("sectionKey");
                    String key = applicationNo + section;
                    Long time = itemJson.getLong("stampedAt");
                    if (!Objects.isNull(time)) {
                        if (!Objects.isNull(obj.getJSONObject(key))) {
                            JSONObject jsonObject = obj.getJSONObject(key);
                            if (time > jsonObject.getLong("stampedAt")) {
                                obj.put(key, itemJson);
                            }
                        } else {
                            obj.put(key, itemJson);
                        }
                    }
                }

                Set<Map.Entry<String, Object>> entries = obj.entrySet();
                for (Map.Entry<String, Object> key: entries) {
                    JSONObject value = (JSONObject) key.getValue();
                    Integer operatorId = value.getInteger("stampedId");
                    for (Chat c : chats) {
                        List<Integer> integers = CHAT.get(c);
                        if (integers.contains(operatorId)) {
                            c.getCon().add(value);
                        }
                    }
                }
                chats.forEach(chat -> chat.getCon().forEach(O -> System.out.println(
                        chat.getName() + ", " +
                                ((JSONObject)O).getString("loanNo") + ", " +
                                ((JSONObject)O).getString("approvedAmount") + ", " +
                                ((JSONObject)O).getString("sectionKey")
                )));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void sql() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        Calendar instance = Calendar.getInstance();
        int month = instance.get(Calendar.MONTH);
        instance.set(Calendar.MONTH, month - 1);
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.MILLISECOND, 0);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        System.out.println(instance.getTime());

        long start = instance.getTimeInMillis();

        instance.set(Calendar.MONTH, month);

        System.out.println(instance.getTime());

        long end = instance.getTimeInMillis();
        System.out.println(String.format(SQL, start, end));
        System.out.println("----------------------------------");
        System.out.println(String.format(SQL_APPROVED, start, end));

    }

    final static String SQL = "SELECT \n" +
            "  t1.loan_no, \n" +
            "  t1.approved_amount, \n" +
            "  t2.section_key, \n" +
            "  t2.stamped_id, \n" +
            "  t2.stamped_at \n" +
            "FROM \n" +
            "  (\n" +
            "    SELECT \n" +
            "      t1.id, \n" +
            "      t1.loan_no, \n" +
            "      t3.approved_amount \n" +
            "    FROM \n" +
            "      loan t1, \n" +
            "      loan_status_data t2, \n" +
            "      payment_option_data t3, \n" +
            "      loan_operation_data t4, \n" +
            "      loan_stamp_data t5 \n" +
            "    WHERE \n" +
            "      t1.id = t2.loan_id \n" +
            "      AND t1.id = t3.loan_id \n" +
            "      AND t1.id = t4.loan_id \n" +
            "      AND t1.id = t5.loan_id \n" +
            "      AND t1.created_at BETWEEN %s \n" +
            "      AND %s \n" +
            "      AND t2.loan_status IN (\n" +
            "        30001, 30002, \n" +
            "        40001, 40002, 40003, \n" +
            "        40102, 40103, 40104, 40105, 40106 \n" +
            "      ) \n" +
            "      AND t4.submitted_id NOT IN (\n" +
            "        2818, 2819, 2820, 2821, 2822, 1171, 1173, 1230, \n" +
            "        639, 3233, 1230, 110299, 4323, 4579, \n" +
            "        2806, 2807, 2813, 2814, 2815, 110460, \n" +
            "        2976, 110569\n" +
            "      ) \n" +
            "      AND t5.stamped_id IN (\n" +
            "        2818, 2819, 2820, 2821, 2822, 1171, 1173, 1230, \n" +
            "        639, 3233, 1230, 110299, 4323, 4579, \n" +
            "        2806, 2807, 2813, 2814, 2815, 110460, \n" +
            "        2976, 110569\n" +
            "      ) group by t1.id \n" +
            "  ) as t1, \n" +
            "  loan_stamp_data t2 \n" +
            "where \n" +
            "  t1.id = t2.loan_id;";
    final static String SQL_APPROVED = "SELECT \n" +
            "  t1.loan_no, \n" +
            "  t3.approved_amount, \n" +
            "  t4.submitted_id, \n" +
            "  t2.loan_status_text \n" +
            "FROM \n" +
            "  loan t1, \n" +
            "  loan_status_data t2, \n" +
            "  payment_option_data t3, \n" +
            "  loan_operation_data t4 \n" +
            "WHERE \n" +
            "  t1.id = t2.loan_id \n" +
            "  AND t1.id = t3.loan_id \n" +
            "  AND t1.id = t4.loan_id \n" +
            "  AND t1.created_at BETWEEN %s \n" +
            "  AND %s \n" +
            "  AND t2.loan_status IN (\n" +
            "        30001, 30002, \n" +
            "        40001, 40002, 40003, \n" +
            "        40102, 40103, 40104, 40105, 40106 \n" +
            "  ) \n" +
            "  AND t4.submitted_id IN (\n" +
            "    2818, 2819, 2820, 2821, 2822, 1171, 1173, 1230, \n" +
            "    639, 3233, 1230, 110299, 4323, 4579, \n" +
            "    2806, 2807, 2813, 2814, 2815, 110460, \n" +
            "    2976, 110569\n" +
            "  ) ORDER BY t4.submitted_id;";
}
