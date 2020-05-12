package com.li.knowledgefarm.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.li.knowledgefarm.entity.QuestionEntity.Completion;
import com.li.knowledgefarm.entity.QuestionEntity.Judgment;
import com.li.knowledgefarm.entity.QuestionEntity.Math23;
import com.li.knowledgefarm.entity.QuestionEntity.Question;
import com.li.knowledgefarm.entity.QuestionEntity.SingleChoice;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FromJson {
    public static List<Question> JsonToEntity(String questionJson){
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<List<Question>>(){
        }.getType(),new JsonDeserializer<List<Question>>(){
            @Override
            public List<Question> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                List<Question> list = new ArrayList<>();
                JsonArray array = json.getAsJsonArray();
                for(JsonElement je : array){
                    String type = je.getAsJsonObject().get("questionType").getAsJsonObject().get("name").getAsString();
                    switch (type){
                        case "填空题":
                            list.add((Question) context.deserialize(je, Completion.class));
                            break;
                        case "选择题":
                            list.add((Question) context.deserialize(je, SingleChoice.class));
                            break;
                        case "判断题":
                            list.add((Question) context.deserialize(je, Judgment.class));
                            break;
                    }
                }
                return list;
            }
        }).create();
        Type type = new TypeToken<List<Question>>() {
        }.getType();
        return gson.fromJson(questionJson,type);
    }
}
