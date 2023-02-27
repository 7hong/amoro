/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netease.arctic.ams.server.optimize;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TableOptimizeItemTest {
  @Test
  public void testGetMaxSequenceLimit() {
    List<TableOptimizeItem.SnapshotFileGroup> sequenceGroups = new ArrayList<>();
    sequenceGroups.add(buildSequenceGroup(1, 100, 2));
    Assert.assertEquals(Long.MIN_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 0));
    Assert.assertEquals(Long.MIN_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 1));
    Assert.assertEquals(Long.MAX_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 2));
    Assert.assertEquals(Long.MAX_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 3));

    sequenceGroups.add(buildSequenceGroup(2, 101, 1));
    Assert.assertEquals(1, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 2));
    Assert.assertEquals(Long.MAX_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 3));

    // disorder
    sequenceGroups.add(buildSequenceGroup(5, 103, 2));
    sequenceGroups.add(buildSequenceGroup(4, 102, 2));
    sequenceGroups.add(buildSequenceGroup(3, 99, 1));
    Assert.assertEquals(Long.MIN_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 2));
    Assert.assertEquals(Long.MIN_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 3));
    Assert.assertEquals(3, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 4));
    Assert.assertEquals(3, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 5));
    Assert.assertEquals(4, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 6));
    Assert.assertEquals(4, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 7));
    Assert.assertEquals(Long.MAX_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 8));
    Assert.assertEquals(Long.MAX_VALUE, TableOptimizeItem.getMaxSequenceKeepingTxIdInOrder(sequenceGroups, 9));

  }

  private static TableOptimizeItem.SnapshotFileGroup buildSequenceGroup(long sequence, long txId, int cnt) {
    return new TableOptimizeItem.SnapshotFileGroup(sequence, txId, cnt);
  }
}