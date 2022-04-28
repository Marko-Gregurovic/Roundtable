package com.example.demo.dto;

public class SortingResponse {

  private Long timeSpentInMs;

  public  SortingResponse(){}

  public SortingResponse(final Long timeSpentInMs) {
    this.timeSpentInMs = timeSpentInMs;
  }

  public Long getTimeSpentInMs() {
    return timeSpentInMs;
  }

  public void setTimeSpentInMs(final Long timeSpentInMs) {
    this.timeSpentInMs = timeSpentInMs;
  }
}
