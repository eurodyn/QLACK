package com.eurodyn.qlack.fuse.search.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The search results obtained after having executed a search
 */
public class QFESSearchResultDTO {

  // The JSON representation of the search result as it comes from ES.
  private String source;

  // The amount of time the query took to be executed (in msec).
  private long executionTime;

  // Indicates whether ES timed out while executing the search.
  private boolean timedOut;

  // Total number of shards that needed to be searched.
  private int shardsTotal;

  // The number of shards successfully searched.
  private int shardsSuccessful;

  // The number of shards failed to be searched.
  private int shardsFailed;

  // The total number of hits for this search.
  private long totalHits;

  // The best score received for this search.
  private float bestScore;

  // An indicator of whether there are more results available (useful in
  // paging).
  private boolean hasMore;

  // If a scroll request was generated then the scroll id is set here
  private String scrollId;

  // The list of hits generated for this search.
  private List<QFESSearchHitDTO> hits = new ArrayList<QFESSearchHitDTO>();

  private Map<String, Long> aggregations = new LinkedHashMap<>();

  /**
   * @return the executionTime
   */
  public long getExecutionTime() {
    return executionTime;
  }

  /**
   * @param executionTime the executionTime to set
   */
  public void setExecutionTime(long executionTime) {
    this.executionTime = executionTime;
  }

  /**
   * @return the timedOut
   */
  public boolean isTimedOut() {
    return timedOut;
  }

  /**
   * @param timedOut the timedOut to set
   */
  public void setTimedOut(boolean timedOut) {
    this.timedOut = timedOut;
  }

  /**
   * @return the shardsTotal
   */
  public int getShardsTotal() {
    return shardsTotal;
  }

  /**
   * @param shardsTotal the shardsTotal to set
   */
  public void setShardsTotal(int shardsTotal) {
    this.shardsTotal = shardsTotal;
  }

  /**
   * @return the shardsSuccessful
   */
  public int getShardsSuccessful() {
    return shardsSuccessful;
  }

  /**
   * @param shardsSuccessful the shardsSuccessful to set
   */
  public void setShardsSuccessful(int shardsSuccessful) {
    this.shardsSuccessful = shardsSuccessful;
  }

  /**
   * @return the shardsFailed
   */
  public int getShardsFailed() {
    return shardsFailed;
  }

  /**
   * @param shardsFailed the shardsFailed to set
   */
  public void setShardsFailed(int shardsFailed) {
    this.shardsFailed = shardsFailed;
  }

  /**
   * @return the totalHits
   */
  public long getTotalHits() {
    return totalHits;
  }

  /**
   * @param totalHits the totalHits to set
   */
  public void setTotalHits(long totalHits) {
    this.totalHits = totalHits;
  }

  /**
   * @return the bestScore
   */
  public float getBestScore() {
    return bestScore;
  }

  /**
   * @param bestScore the bestScore to set
   */
  public void setBestScore(float bestScore) {
    this.bestScore = bestScore;
  }

  /**
   * @return the source
   */
  public String getSource() {
    return source;
  }

  /**
   * @param source the source to set
   */
  public void setSource(String source) {
    this.source = source;
  }

  public void addHit(QFESSearchHitDTO dto) {
    hits.add(dto);
  }

  /**
   * @return the hits
   */
  public List<QFESSearchHitDTO> getHits() {
    return hits;
  }

  /**
   * @param hits the hits to set
   */
  public void setHits(List<QFESSearchHitDTO> hits) {
    this.hits = hits;
  }

  /**
   * @return the hasMore
   */
  public boolean isHasMore() {
    return hasMore;
  }

  /**
   * @param hasMore the hasMore to set
   */
  public void setHasMore(boolean hasMore) {
    this.hasMore = hasMore;
  }

  public String getScrollId() {
    return scrollId;
  }

  public void setScrollId(String scrollId) {
    this.scrollId = scrollId;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "QFESSearchResultDTO [source=" + source + ", executionTime=" + executionTime
        + ", timedOut="
        + timedOut
        + ", shardsTotal=" + shardsTotal + ", shardsSuccessful=" + shardsSuccessful
        + ", shardsFailed="
        + shardsFailed + ", totalHits=" + totalHits + ", bestScore=" + bestScore + ", hasMore="
        + hasMore
        + ", hits=" + hits + "]";
  }

  public Map<String, Long> getAggregations() {
    return aggregations;
  }

  public void setAggregations(Map<String, Long> aggregations) {
    this.aggregations = aggregations;
  }
}
