package com.seneca.apd.finalproject.hotelreservation.InterFaces;

import java.util.Map;

public interface IFeedback {
    boolean submitFeedback();
    Map<String, String> getFeedbackDetails();
}
