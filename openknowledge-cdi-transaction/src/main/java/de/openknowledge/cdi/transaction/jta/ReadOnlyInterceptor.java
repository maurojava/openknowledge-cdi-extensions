/*
 * Copyright open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.openknowledge.cdi.transaction.jta;

import de.openknowledge.cdi.transaction.ReadOnly;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.UserTransaction;
import java.io.Serializable;

/**
 * @author Jens Schumann - open knowledge GmbH
 */
@ReadOnly
@Interceptor
public class ReadOnlyInterceptor implements Serializable {

  @Inject
  private UserTransactionHolder utTransactionHolder;

  public ReadOnlyInterceptor() {
  }

  public ReadOnlyInterceptor(UserTransaction aTx) {
    utTransactionHolder = new DefaultUserTransactionHolder(aTx);
  }

  @AroundInvoke
  public Object markReadOnly(InvocationContext ic) throws Exception {
    try {
      return ic.proceed();
    } finally {
      utTransactionHolder.getUserTransaction().setRollbackOnly();
    }
  }

}
