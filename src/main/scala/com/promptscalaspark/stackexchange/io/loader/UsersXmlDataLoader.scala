/*
 * Copyright © 2019 Abhishek Verma (abhishekv3007@gmail.com)
 *
 *                    GNU AFFERO GENERAL PUBLIC LICENSE
 *                       Version 3, 19 November 2007
 * Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *                            Preamble
 *
 *   The GNU Affero General Public License is a free, copyleft license for
 * software and other kinds of works, specifically designed to ensure
 * cooperation with the community in the case of network server software.
 *
 *   The licenses for most software and other practical works are designed
 * to take away your freedom to share and change the works.  By contrast,
 * our General Public Licenses are intended to guarantee your freedom to
 * share and change all versions of a program--to make sure it remains free
 * software for all its users.
 */

package com.promptscalaspark.stackexchange.io.loader

import com.promptscalaspark.stackexchange.api.LoaderHelper
import com.promptscalaspark.stackexchange.io.ioSchema.StackExchangeInputSchema.UsersData
import org.apache.spark.SparkConf
import org.apache.spark.sql.functions.{col, explode}
import org.apache.spark.sql.{Dataset, Encoders, SparkSession}

object UsersXmlDataLoader {
  def loadUsersDS(path: String): Dataset[UsersData] = {

    val usersXmlPath = path + "Users.xml"

    val sparkConf = new SparkConf()
      .setAppName("stackExchange-spark-analyzer")
      .set("spark.driver.allowMultipleContexts", "true")

    val spark =
      SparkSession
        .builder()
        .config(sparkConf)
        .master("local[*]")
        .getOrCreate()

    val usersRawDF = spark.read
      .option("rowTag", "users")
      .format("xml")
      .load(usersXmlPath)

    val explodedMappedUserData = usersRawDF
      .select(explode(col("row")))
      .select(
        LoaderHelper
          .getMembers[UsersData]
          .map(x => col("col._" + x)): _*)

    val usersDataset: Dataset[UsersData] =
      LoaderHelper
        .removeSpecialCharsFromCols(explodedMappedUserData, "_", "")
        .as[UsersData](Encoders.product)
        .cache()

    usersDataset
  }

}
