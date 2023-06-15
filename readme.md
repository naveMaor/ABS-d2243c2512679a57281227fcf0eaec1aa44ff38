![System Architecture Diagram](https://pub.mdpi-res.com/sustainability/sustainability-15-04028/article_deploy/html/images/sustainability-15-04028-g001.png?1677075418)

**Alternative Banking System - P2P** 

Peer-to-peer (P2P) lending enables individuals to obtain loans directly from other individuals, cutting out the financial institution as the middleman.
Websites that facilitate P2P lending have greatly increased its adoption as an alternative method of financing. P2P lending is also known as “social lending” or “crowd lending.”

This project is a desktop application, aims to build an Alternative Banking System that connects lenders and borrowers, providing a platform for loans with low interest rates for borrowers and safe investments for lenders. The system will manage customer accounts, loan applications, loan placements, repayments, and loan lifecycle tracking. The implementation is in Java 8 and utilize XML files to configure the system's behavior.

**Features:**

1. Customer Management:
   - Customers can function as both borrowers and lenders.
   - Each customer has a unique identifier name and an internal account.
   - Customers can deposit or withdraw funds from their accounts.

2. Loan Application:
   - Borrowers can apply for loans, specifying loan details such as the loan amount, interest rate, category, and repayment period.
   - Loans are characterized by a unique name and are associated with the customer who submitted the application.
   - Loan repayments can be made in custom-defined intervals.

3. Loan Placement Algorithm:
   - Lenders can define investment parameters, including the total investment amount, preferred loan categories, minimum acceptable interest rate, minimum loan duration, maximum percentage of loan ownership, and maximum open loans per borrower.
   - The system uses a placement algorithm to suggest loan investments that meet the lender's criteria.
   - Investments are divided equally among eligible loans, reducing the risk for lenders.

4. Loan Lifecycle:
   - Loans progress through various stages: New, Pending, Active, In Risk, and Finished.
   - Loans become Pending when lenders contribute funds to the loan.
   - Loans become Active when the required funds are collected, and the loan amount is transferred to the borrower.
   - Loans can enter In Risk if the borrower fails to make full payments.
   - Loans return to Active when the borrower pays the accumulated debt in full.
   - Loans move to Finished status when they are fully repaid or closed by the borrower.

5. Loan Selling and Closing:
   - Borrowers can close an existing loan by paying the principal and required interest to all lenders.
   - Lenders can sell their loans, transferring ownership to other users who pay the remaining loan value.
   - Loan transactions between lenders and borrowers are transparent within the system.

**USING Guidelines:**

- Import a system configuration from an XML file or create your own system.




