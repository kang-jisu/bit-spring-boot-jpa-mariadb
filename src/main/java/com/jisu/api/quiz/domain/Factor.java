package com.jisu.api.quiz.domain;

public enum Factor {
    MINIMUM, MAXIMUM;

    @Override
    public String toString(){
        String value = "";
        switch (this){
            case MAXIMUM:value = String.valueOf(99); break;
            case MINIMUM:value = String.valueOf(11); break;
        }
        return value;
    }
}
