package consultation.rumpilstilstkin.ru.lesson7;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import consultation.rumpilstilstkin.ru.lesson7.data.models.RepsModel;
import consultation.rumpilstilstkin.ru.lesson7.data.rest.NetApiClientInterface;
import consultation.rumpilstilstkin.ru.lesson7.presenter.RepsPresenter;
import consultation.rumpilstilstkin.ru.lesson7.presenter.RepsView;
import consultation.rumpilstilstkin.ru.lesson7.presenter.RepsView$$State;
import io.reactivex.Single;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RepoPresenterTest {

    private RepsPresenter presenter;
    @Mock
    RepsView$$State repsViewState;
    @Mock
    private RepsView view;

    @Mock private NetApiClientInterface client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new RepsPresenter(client);
    }

    @Test
    public void testShowList(){
        RepsModel model = new RepsModel();
        List<RepsModel> list = new ArrayList<>();
        list.add(model);
        when(client.getReps()).thenReturn(Single.just(list));
        presenter.attachView(view);
        presenter.setViewState(repsViewState);
        verify(view).showLoading();
        verify(view).showRepoList(list);
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testShowError(){
        Exception error = new IOException();
        when(client.getReps()).thenReturn(Single.fromCallable(() -> { throw error; }));
        presenter.attachView(view);
        presenter.setViewState(repsViewState);
        verify(view).showLoading();
        verify(view).showError(error);
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }

    @Test
    public void testShowEmpty(){
        List<RepsModel> list = new ArrayList<>();

        when(client.getReps()).thenReturn(Single.fromCallable(() -> list));
        presenter.attachView(view);
        presenter.setViewState(repsViewState);

        verify(view).showLoading();
        verify(view).showEmptyState();
        verify(view).hideLoading();
        verifyNoMoreInteractions(view);
    }
}
