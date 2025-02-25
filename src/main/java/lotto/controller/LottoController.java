package lotto.controller;

import lotto.domain.Lotto;
import lotto.domain.Money;
import lotto.domain.ReceivedPrize;
import lotto.domain.WinningLotto;
import lotto.dto.LottoResultDto;
import lotto.model.AutoLottoGenerator;
import lotto.view.InputView;
import lotto.view.OutputView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LottoController {
    public LottoController() {
    }

    public void play() {
        Money money = getUserMoneyInput();
        List<Lotto> lottos = issueUserBoughtLotto(money.calculateBoughtLottoCount());
        showIssuedLottos(lottos);
        WinningLotto winningLotto = issueWinningLotto();
        ReceivedPrize receivedPrize = calculatePrize(lottos, winningLotto);
        showLottoResult(receivedPrize, money.getMoney());
    }

    private Money getUserMoneyInput() {
        return new Money(InputView.inputMoney());
    }

    private List<Lotto> issueUserBoughtLotto(int lottoCount) {
        List<Lotto> lottos = new ArrayList<>();
        generateAutoLotto(lottos, lottoCount);
        return lottos;
    }

    private void showIssuedLottos(List<Lotto> lottos) {
        OutputView.printIssuedLotto(transferLottoToNumbers(lottos));
    }
    private List<Lotto> generateAutoLotto(List<Lotto> lottos, int lottoCount) {
        while (lottos.size() < lottoCount) {
            lottos.add(AutoLottoGenerator.objectLotto());
        }
        return lottos;
    }

    private List<List<Integer>> transferLottoToNumbers(List<Lotto> lottos) {
        return lottos.stream()
                .map(Lotto::getSortedNumbers)
                .collect(Collectors.toList());
    }

    private WinningLotto issueWinningLotto() {
        Lotto lotto = new Lotto(InputView.inputLottoNum());
        int bonusNumber = InputView.inputBonusNum();
        return new WinningLotto(lotto, bonusNumber);
    }

    private ReceivedPrize calculatePrize(List<Lotto> lottos, WinningLotto winningLotto) {
        return new ReceivedPrize(lottos, winningLotto);
    }

    public void showLottoResult(ReceivedPrize receivedPrize, int money) {
        LottoResultDto resultDto = LottoResultDto.of(receivedPrize,
                receivedPrize.calculateRateOfReturn(money));
        OutputView.printLottoResult(resultDto);
    }
}
