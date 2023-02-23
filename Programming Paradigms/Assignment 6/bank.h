#ifndef _BANK_H
#define _BANK_H
#include <semaphore.h>



typedef struct Bank {
  unsigned int numberBranches;
  struct Branch  *branches;
  struct Report  *report;
  int workers;
  pthread_mutex_t lock;
  sem_t finished;
  pthread_mutex_t reportLock;
} Bank;

#include "account.h"

int Bank_Balance(Bank *bank, AccountAmount *balance);

Bank *Bank_Init(int numBranches, int numAccounts, AccountAmount initAmount,
                AccountAmount reportingAmount,
                int numWorkers);

int Bank_Validate(Bank *bank);
int Bank_Compare(Bank *bank1, Bank *bank2);



#endif /* _BANK_H */
