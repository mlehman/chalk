/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package chalk.tools.formats.ad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

import chalk.tools.formats.ad.ADTokenSampleStreamFactory;
import chalk.tools.tokenize.TokenSample;
import chalk.tools.util.ObjectStream;

public class ADTokenSampleStreamTest {

  List<TokenSample> samples = new ArrayList<TokenSample>();

  @Test
  public void testSimpleCount() throws IOException {
    assertEquals(ADParagraphStreamTest.NUM_SENTENCES, samples.size());
  }

  @Test
  public void testSentences() throws IOException {
    assertTrue(samples.get(5).getText().contains("ofereceu-me"));
  }

  @Before
  public void setup() throws IOException, URISyntaxException {
    ADTokenSampleStreamFactory factory = new ADTokenSampleStreamFactory(
        ADTokenSampleStreamFactory.Parameters.class);

    File dict = new File(getClass().getClassLoader()
        .getResource("chalk/tools/tokenize/latin-detokenizer.xml").toURI());
    File data = new File(getClass().getClassLoader()
        .getResource("chalk/tools/formats/ad.sample").toURI());
    String[] args = { "-data", data.getCanonicalPath(), "-encoding", "UTF-8",
        "-lang", "pt", "-detokenizer", dict.getCanonicalPath() };
    ObjectStream<TokenSample> tokenSampleStream = factory.create(args);

    TokenSample sample = tokenSampleStream.read();

    while (sample != null) {
      samples.add(sample);
      sample = tokenSampleStream.read();
    }

  }

}
