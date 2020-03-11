package model;

public class PieChartVO {
   private String survey;
   private int count;
   public PieChartVO(String survey, int count) {
      super();
      this.survey = survey;
      this.count = count;
   }
   public String getSurvey() {
      return survey;
   }
   public void setSurvey(String survey) {
      this.survey = survey;
   }
   public int getCount() {
      return count;
   }
   public void setCount(int count) {
      this.count = count;
   }
   
   
}