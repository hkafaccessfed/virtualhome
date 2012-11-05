package aaf.vhr

import edu.vt.middleware.password.*
import edu.vt.middleware.dictionary.*
import edu.vt.middleware.dictionary.sort.*

class PasswordValidationService {
  boolean transactional = true
  def grailsApplication
  def cryptoService

  ArrayWordList awl
  Properties msgs

  public PasswordValidationService() {
    def readers = new java.io.Reader[1]
    readers[0] = new InputStreamReader(getClass().getResourceAsStream('dictionary-web2.txt'))
    awl = WordLists.createFromReader(readers, false, new ArraysSort());

    msgs = new Properties()
    msgs.load(getClass().getResourceAsStream("passwordvalidation.txt"))
  }

  /*
    @pre: The ManagedSubject plainPassword and plainPasswordConfirm
    values have been populated with a plain text representation
    of the password attempting to be set by the subject

    NIST 800-63-1
    http://csrc.nist.gov/publications/nistpubs/800-63-1/SP-800-63-1.pdf
    Memorized Secret Token
    Level 2

    The memorized secret may be a randomly generated PIN consisting 
    of 6 or more digits, a user generated string consisting of 8 or 
    more characters chosen from an alphabet of 90 or more characters, 
    or a secret with equivalent entropy.

    CSP implements dictionary or composition rule to constrain 
    usergenerated secrets.

    8 char dict + composition = 30 entropy - Table A.1
    16 char = 30 entropy - Table A.1

    Thus we enforce:
      All passwords:
      Do not contain the login name
      Minimum length 8 char
      No whitespace
      No alphabetic sequences
      No numerical sequences
      No qwerty sequences
      No more then 3 repeat characters

      Password is 8 - 15 char:
      At least 1 number
      At least 1 Non Alpha
      At least 1 Uppercase Character
      At least 1 Lowercase Chatacter
      No dictionary words (dictionary file contains 235924 words)

      Password is 16 char or greater:
      Minimum length 16 char
  */
  def validate(ManagedSubject subject) {
    if(subject.plainPassword != subject.plainPasswordConfirmation) {
      log.warn("The submitted plaintext passwords for $subject don't match, rejecting.")
      return [false, ['aaf.vhr.passwordvalidationservice.notmatching']]
    }

    if(subject.plainPassword.toLowerCase().contains(subject.login.toLowerCase())) {
      log.warn("The submitted plaintext passwords for $subject don't match, rejecting.")
      return [false, ['aaf.vhr.passwordvalidationservice.containslogin']]
    }

    println subject.hash
    if(subject.hash != null) {
      if(cryptoService.verifyPasswordHash(subject.plainPassword, subject)) {
        log.warn("The submitted plaintext password for $subject is the same as the current value, rejecting.")
        return [false, ['aaf.vhr.passwordvalidationservice.reused']]
      }
    }

    def pw = subject.plainPassword

    MessageResolver resolver = new MessageResolver(msgs)

    WhitespaceRule whitespaceRule = new WhitespaceRule()
    AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule(3, false)
    NumericalSequenceRule numSeqRule = new NumericalSequenceRule(3, false)
    QwertySequenceRule qwertySeqRule = new QwertySequenceRule(3, false)
    RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(3)

    List<Rule> ruleList = new ArrayList<Rule>()
    ruleList.add(whitespaceRule)
    ruleList.add(alphaSeqRule)
    ruleList.add(numSeqRule)
    ruleList.add(qwertySeqRule)
    ruleList.add(repeatRule)

    if(pw.length() < 16) {
      LengthRule lengthRule = new LengthRule()
      lengthRule.minimumLength = 8

      CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule()
      charRule.getRules().add(new DigitCharacterRule(1))
      charRule.getRules().add(new NonAlphanumericCharacterRule(1))
      charRule.getRules().add(new UppercaseCharacterRule(1))
      charRule.getRules().add(new LowercaseCharacterRule(1))
      charRule.setNumberOfCharacteristics(4)

      WordListDictionary dict = new WordListDictionary(awl);
      DictionarySubstringRule dictRule = new DictionarySubstringRule(dict)
      dictRule.setWordLength(4)
      dictRule.setMatchBackwards(true)

      ruleList.add(lengthRule)
      ruleList.add(charRule)
      ruleList.add(dictRule)
    } else {
      LengthRule lengthRule = new LengthRule()
      lengthRule.minimumLength = 16

      ruleList.add(lengthRule)
    }

    PasswordValidator validator = new PasswordValidator(resolver, ruleList)
    PasswordData passwordData = new PasswordData(new Password(pw))

    RuleResult result = validator.validate(passwordData)

    if(result.isValid())
      [true, null]
    else
      [false, validator.getMessages(result)]
  }
}
