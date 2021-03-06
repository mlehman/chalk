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

package chalk.tools.cmdline.sentdetect;

import java.io.IOException;

import chalk.tools.cmdline.AbstractCrossValidatorTool;
import chalk.tools.cmdline.CmdLineUtil;
import chalk.tools.cmdline.TerminateToolException;
import chalk.tools.cmdline.params.CVParams;
import chalk.tools.cmdline.sentdetect.SentenceDetectorCrossValidatorTool.CVToolParams;
import chalk.tools.dictionary.Dictionary;
import chalk.tools.sentdetect.SDCrossValidator;
import chalk.tools.sentdetect.SentenceDetectorEvaluationMonitor;
import chalk.tools.sentdetect.SentenceDetectorFactory;
import chalk.tools.sentdetect.SentenceSample;
import chalk.tools.util.eval.FMeasure;
import chalk.tools.util.model.ModelUtil;


public final class SentenceDetectorCrossValidatorTool
    extends AbstractCrossValidatorTool<SentenceSample, CVToolParams> {
  
  interface CVToolParams extends TrainingParams, CVParams {
  }

  public SentenceDetectorCrossValidatorTool() {
    super(SentenceSample.class, CVToolParams.class);
  }

  public String getShortDescription() {
    return "K-fold cross validator for the learnable sentence detector";
  }
  
  public void run(String format, String[] args) {
    super.run(format, args);

    mlParams = CmdLineUtil.loadTrainingParameters(params.getParams(), false);
    if (mlParams == null) {
      mlParams = ModelUtil.createTrainingParameters(params.getIterations(), params.getCutoff());
    }

    SDCrossValidator validator;
    
    SentenceDetectorEvaluationMonitor errorListener = null;
    if (params.getMisclassified()) {
      errorListener = new SentenceEvaluationErrorListener();
    }

    char[] eos = null;
    if (params.getEosChars() != null)
      eos = params.getEosChars().toCharArray();

    try {
      Dictionary abbreviations = SentenceDetectorTrainerTool.loadDict(params.getAbbDict());
      SentenceDetectorFactory sdFactory = SentenceDetectorFactory.create(
          params.getFactory(), factory.getLang(), true, abbreviations, eos);
      validator = new SDCrossValidator(factory.getLang(), mlParams, sdFactory,
          errorListener);
      
      validator.evaluate(sampleStream, params.getFolds());
    }
    catch (IOException e) {
      throw new TerminateToolException(-1, "IO error while reading training data or indexing data: " +
          e.getMessage(), e);
    }
    finally {
      try {
        sampleStream.close();
      } catch (IOException e) {
        // sorry that this can fail
      }
    }
    
    FMeasure result = validator.getFMeasure();
    
    System.out.println(result.toString());
  }
}
