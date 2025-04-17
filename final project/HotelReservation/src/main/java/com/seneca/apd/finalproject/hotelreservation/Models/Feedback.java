package com.seneca.apd.finalproject.hotelreservation.Models;

import com.seneca.apd.finalproject.hotelreservation.InterFaces.IFeedback;

import java.util.Map;

public class Feedback implements IFeedback {
    private int feedbackID;
    private int guestID;
    private int reservationID;
    private String comments;
    private float rating;

    public Feedback(int feedbackID, int guestID, int reservationID, String comments, float rating) {
        this.feedbackID = feedbackID;
        this.guestID = guestID;
        this.reservationID = reservationID;
        this.comments = comments;
        this.rating = rating;
    }

    @Override
    public boolean submitFeedback() {
        return true;
    }

    @Override
    public Map<String, String> getFeedbackDetails() {
        return Map.of(
                "FeedbackID", String.valueOf(feedbackID),
                "GuestID", String.valueOf(guestID),
                "ReservationID", String.valueOf(reservationID),
                "Comments", comments,
                "Rating", String.valueOf(rating)
        );
    }
}
